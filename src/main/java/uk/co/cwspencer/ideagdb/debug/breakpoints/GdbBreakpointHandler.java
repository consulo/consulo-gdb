package uk.co.cwspencer.ideagdb.debug.breakpoints;

import consulo.application.AllIcons;
import consulo.execution.debug.XSourcePosition;
import consulo.execution.debug.breakpoint.XBreakpointHandler;
import consulo.execution.debug.breakpoint.XBreakpointProperties;
import consulo.execution.debug.breakpoint.XLineBreakpoint;
import consulo.logging.Logger;
import consulo.util.collection.BidirectionalMap;
import org.jetbrains.annotations.NotNull;
import uk.co.cwspencer.gdb.Gdb;
import uk.co.cwspencer.gdb.messages.GdbBreakpoint;
import uk.co.cwspencer.gdb.messages.GdbErrorEvent;
import uk.co.cwspencer.gdb.messages.GdbEvent;
import uk.co.cwspencer.ideagdb.debug.GdbDebugProcess;

import java.util.List;

public class GdbBreakpointHandler extends XBreakpointHandler<XLineBreakpoint<XBreakpointProperties>>
{
	private static final Logger m_log = Logger.getInstance(GdbBreakpointHandler.class);

	private Gdb m_gdb;
	private GdbDebugProcess m_debugProcess;

	// The breakpoints that have been set and their GDB breakpoint numbers
	private final BidirectionalMap<Integer, XLineBreakpoint<XBreakpointProperties>> m_breakpoints = new BidirectionalMap<Integer,
			XLineBreakpoint<XBreakpointProperties>>();

	public GdbBreakpointHandler(Gdb gdb, GdbDebugProcess debugProcess)
	{
		super(GdbBreakpointType.class);
		m_gdb = gdb;
		m_debugProcess = debugProcess;
	}

	/**
	 * Registers the given breakpoint with GDB.
	 *
	 * @param breakpoint The breakpoint.
	 */
	@Override
	public void registerBreakpoint(
			@NotNull final XLineBreakpoint<XBreakpointProperties> breakpoint)
	{
		// TODO: I think we can use tracepoints here if the suspend policy isn't to stop the process

		// Check if the breakpoint already exists
		Integer number = findBreakpointNumber(breakpoint);
		if(number != null)
		{
			// Re-enable the breakpoint
			m_gdb.sendCommand("-break-enable " + number);
		}
		else
		{
			// Set the breakpoint
			XSourcePosition sourcePosition = breakpoint.getSourcePosition();
			String command = "-break-insert -f " + sourcePosition.getFile().getPath() + ":" +
					(sourcePosition.getLine() + 1);
			m_gdb.sendCommand(command, new Gdb.GdbEventCallback()
			{
				@Override
				public void onGdbCommandCompleted(GdbEvent event)
				{
					onGdbBreakpointReady(event, breakpoint);
				}
			});
		}
	}

	/**
	 * Unregisters the given breakpoint with GDB.
	 *
	 * @param breakpoint The breakpoint.
	 * @param temporary  Whether we are deleting the breakpoint or temporarily disabling it.
	 */
	@Override
	public void unregisterBreakpoint(
			@NotNull XLineBreakpoint<XBreakpointProperties> breakpoint, boolean temporary)
	{
		Integer number = findBreakpointNumber(breakpoint);
		if(number == null)
		{
			m_log.error("Cannot remove breakpoint; could not find it in breakpoint table");
			return;
		}

		if(!temporary)
		{
			// Delete the breakpoint
			m_gdb.sendCommand("-break-delete " + number);
			synchronized(m_breakpoints)
			{
				m_breakpoints.remove(number);
			}
		}
		else
		{
			// Disable the breakpoint
			m_gdb.sendCommand("-break-disable " + number);
		}
	}

	/**
	 * Finds a breakpoint by its GDB number.
	 *
	 * @param number The GDB breakpoint number.
	 * @return The breakpoint, or null if it could not be found.
	 */
	public XLineBreakpoint<XBreakpointProperties> findBreakpoint(int number)
	{
		synchronized(m_breakpoints)
		{
			return m_breakpoints.get(number);
		}
	}

	/**
	 * Finds a breakpoint's GDB number.
	 *
	 * @param breakpoint The breakpoint to search for.
	 * @return The breakpoint number, or null if it could not be found.
	 */
	public Integer findBreakpointNumber(XLineBreakpoint<XBreakpointProperties> breakpoint)
	{
		List<Integer> numbers;
		synchronized(m_breakpoints)
		{
			numbers = m_breakpoints.getKeysByValue(breakpoint);
		}

		if(numbers == null || numbers.isEmpty())
		{
			return null;
		}
		else if(numbers.size() > 1)
		{
			m_log.warn("Found multiple breakpoint numbers for breakpoint; only returning the " + "first");
		}
		return numbers.get(0);
	}

	/**
	 * Callback function for when GDB has responded to our breakpoint request.
	 *
	 * @param event      The event.
	 * @param breakpoint The breakpoint we tried to set.
	 */
	private void onGdbBreakpointReady(
			GdbEvent event, XLineBreakpoint<XBreakpointProperties> breakpoint)
	{
		if(event instanceof GdbErrorEvent)
		{
			m_debugProcess.getSession().updateBreakpointPresentation(breakpoint, AllIcons.Debugger.Db_invalid_breakpoint,
					((GdbErrorEvent) event).message);
			return;
		}
		if(!(event instanceof GdbBreakpoint))
		{
			m_debugProcess.getSession().updateBreakpointPresentation(breakpoint, AllIcons.Debugger.Db_invalid_breakpoint,
					"Unexpected data received from GDB");
			m_log.warn("Unexpected event " + event + " received from -break-insert request");
			return;
		}

		// Save the breakpoint
		GdbBreakpoint gdbBreakpoint = (GdbBreakpoint) event;
		if(gdbBreakpoint.number == null)
		{
			m_debugProcess.getSession().updateBreakpointPresentation(breakpoint, AllIcons.Debugger.Db_invalid_breakpoint,
					"No breakpoint number received from GDB");
			m_log.warn("No breakpoint number received from GDB after -break-insert request");
			return;
		}

		synchronized(m_breakpoints)
		{
			m_breakpoints.put(gdbBreakpoint.number, breakpoint);
		}

		// Mark the breakpoint as set
		// TODO: Don't do this yet if the breakpoint is pending
		m_debugProcess.getSession().updateBreakpointPresentation(breakpoint, AllIcons.Debugger.Db_verified_breakpoint, null);
	}
}
