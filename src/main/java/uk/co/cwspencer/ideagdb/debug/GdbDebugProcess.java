package uk.co.cwspencer.ideagdb.debug;

import consulo.execution.debug.XDebugProcess;
import consulo.execution.debug.XDebugSession;
import consulo.execution.debug.XSourcePosition;
import consulo.execution.debug.breakpoint.XBreakpoint;
import consulo.execution.debug.breakpoint.XBreakpointHandler;
import consulo.execution.debug.breakpoint.XBreakpointProperties;
import consulo.execution.debug.evaluation.XDebuggerEditorsProvider;
import consulo.execution.debug.ui.XDebugTabLayouter;
import consulo.execution.icon.ExecutionIconGroup;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.ExecutionConsole;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.ConsoleViewContentType;
import consulo.execution.ui.layout.PlaceInGrid;
import consulo.execution.ui.layout.RunnerLayoutUi;
import consulo.logging.Logger;
import consulo.ui.ex.action.ActionPlaces;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.DefaultActionGroup;
import consulo.ui.ex.content.Content;
import org.jetbrains.annotations.NotNull;
import uk.co.cwspencer.gdb.Gdb;
import uk.co.cwspencer.gdb.GdbListener;
import uk.co.cwspencer.gdb.gdbmi.GdbMiResultRecord;
import uk.co.cwspencer.gdb.gdbmi.GdbMiStreamRecord;
import uk.co.cwspencer.gdb.messages.*;
import uk.co.cwspencer.ideagdb.debug.breakpoints.GdbBreakpointHandler;
import uk.co.cwspencer.ideagdb.run.GdbRunConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GdbDebugProcess extends XDebugProcess implements GdbListener
{
	private static final Logger LOGGER = Logger.getInstance(GdbDebugProcess.class);

	private GdbDebuggerEditorsProvider m_editorsProvider = new GdbDebuggerEditorsProvider();
	private ConsoleView myConsole;

	// The run configuration
	private GdbRunConfiguration myRunProfile;

	// The GDB console
	private GdbConsoleView myGdbConsole;

	// The GDB instance
	private Gdb myGdb;

	// The breakpoint handler
	private GdbBreakpointHandler myBreakpointHandler;

	// Time formatter
	private SimpleDateFormat m_timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

	/**
	 * Constructor; launches GDB.
	 */
	public GdbDebugProcess(XDebugSession session, ExecutionEnvironment env, ExecutionConsole executionConsole)
	{
		super(session);
		myRunProfile = (GdbRunConfiguration) env.getRunProfile();
		myConsole = (ConsoleView) executionConsole;

		// Prepare GDB
		myGdb = new Gdb(myRunProfile.GDB_PATH, myRunProfile.getWorkingDirectory(), this);

		// Create the GDB console
		myGdbConsole = new GdbConsoleView(myGdb, session.getProject());

		// Create the breakpoint handler
		myBreakpointHandler = new GdbBreakpointHandler(myGdb, this);

		// Launch the process
		myGdb.start();
	}

	@NotNull
	@Override
	public XBreakpointHandler<?>[] getBreakpointHandlers()
	{
		return new GdbBreakpointHandler[]{myBreakpointHandler};
	}

	@NotNull
	@Override
	public XDebuggerEditorsProvider getEditorsProvider()
	{
		return m_editorsProvider;
	}

	/**
	 * Steps over the next line.
	 */
	@Override
	public void startStepOver()
	{
		myGdb.sendCommand("-exec-next");
	}

	@Override
	public void startPausing()
	{
		// TODO: GDB doesn't support handling commands when the target is running; we should use
		// asynchronous mode if the target supports it. I'm not really sure how to deal with this on
		// other targets (e.g., Windows)
		LOGGER.warn("startPausing: stub");
	}

	/**
	 * Steps into the next line.
	 */
	@Override
	public void startStepInto()
	{
		myGdb.sendCommand("-exec-step");
	}

	/**
	 * Steps out of the current function.
	 */
	@Override
	public void startStepOut()
	{
		myGdb.sendCommand("-exec-finish");
	}

	/**
	 * Stops program execution and exits GDB.
	 */
	@Override
	public void stop()
	{
		myGdb.sendCommand("-gdb-exit");
	}

	/**
	 * Resumes program execution.
	 */
	@Override
	public void resume()
	{
		myGdb.sendCommand("-exec-continue --all");
	}

	@Override
	public void runToPosition(@NotNull XSourcePosition position)
	{
		LOGGER.warn("runToPosition: stub");
	}

	@NotNull
	@Override
	public ExecutionConsole createConsole()
	{
		return myConsole;
	}

	@NotNull
	@Override
	public XDebugTabLayouter createTabLayouter()
	{
		return new XDebugTabLayouter()
		{
			@Override
			public void registerAdditionalContent(@NotNull RunnerLayoutUi ui)
			{
				Content gdbConsoleContent = ui.createContent("GdbConsoleContent", myGdbConsole.getComponent(), "GDB Console", ExecutionIconGroup.console(),
						myGdbConsole.getPreferredFocusableComponent());
				gdbConsoleContent.setCloseable(false);

				// Create the actions
				final DefaultActionGroup consoleActions = new DefaultActionGroup();
				AnAction[] actions = myGdbConsole.getConsole().createConsoleActions();
				for(AnAction action : actions)
				{
					consoleActions.add(action);
				}
				gdbConsoleContent.setActions(consoleActions, ActionPlaces.DEBUGGER_TOOLBAR, myGdbConsole.getConsole().getPreferredFocusableComponent());

				ui.addContent(gdbConsoleContent, 2, PlaceInGrid.bottom, false);
			}
		};
	}

	/**
	 * Called when a GDB error occurs.
	 *
	 * @param ex The exception
	 */
	@Override
	public void onGdbError(final Throwable ex)
	{
		LOGGER.error("GDB error", ex);
	}

	/**
	 * Called when GDB has started.
	 */
	@Override
	public void onGdbStarted()
	{
		// Send startup commands
		String[] commandsArray = myRunProfile.STARTUP_COMMANDS.split("\\r?\\n");
		for(String command : commandsArray)
		{
			command = command.trim();
			if(!command.isEmpty())
			{
				myGdb.sendCommand(command);
			}
		}
	}

	/**
	 * Called whenever a command is sent to GDB.
	 *
	 * @param command The command that was sent.
	 * @param token   The token the command was sent with.
	 */
	@Override
	public void onGdbCommandSent(String command, long token)
	{
		myGdbConsole.getConsole().print(m_timeFormat.format(new Date()) + " " + token + "> " +
				command + "\n", ConsoleViewContentType.USER_INPUT);
	}

	/**
	 * Called when a GDB event is received.
	 *
	 * @param event The event.
	 */
	@Override
	public void onGdbEventReceived(GdbEvent event)
	{
		if(event instanceof GdbStoppedEvent)
		{
			// Target has stopped
			onGdbStoppedEvent((GdbStoppedEvent) event);
		}
		else if(event instanceof GdbRunningEvent)
		{
			// Target has started
			getSession().sessionResumed();
		}
	}

	/**
	 * Handles a 'target stopped' event from GDB.
	 *
	 * @param event The event
	 */
	private void onGdbStoppedEvent(final GdbStoppedEvent event)
	{
		if(myGdb.hasCapability("thread-info"))
		{
			// Get information about the threads
			myGdb.sendCommand("-thread-info", new Gdb.GdbEventCallback()
			{
				@Override
				public void onGdbCommandCompleted(GdbEvent threadInfoEvent)
				{
					onGdbThreadInfoReady(threadInfoEvent, event);
				}
			});
		}
		else
		{
			// Handle it immediately without any thread data
			handleTargetStopped(event, null);
		}
	}

	/**
	 * Called when a stream record is received.
	 *
	 * @param record The record.
	 */
	@Override
	public void onStreamRecordReceived(GdbMiStreamRecord record)
	{
		// Log the record
		switch(record.type)
		{
			case Console:
				StringBuilder sb = new StringBuilder();
				if(record.userToken != null)
				{
					sb.append("<");
					sb.append(record.userToken);
					sb.append(" ");
				}
				sb.append(record.message);
				myGdbConsole.getConsole().print(sb.toString(), ConsoleViewContentType.NORMAL_OUTPUT);
				break;

			case Target:
				myConsole.print(record.message, ConsoleViewContentType.NORMAL_OUTPUT);
				break;

			case Log:
				myGdbConsole.getConsole().print(record.message, ConsoleViewContentType.SYSTEM_OUTPUT);
				break;
		}
	}

	/**
	 * Called when a result record is received.
	 *
	 * @param record The record.
	 */
	@Override
	public void onResultRecordReceived(GdbMiResultRecord record)
	{
		// Log the record
		StringBuilder sb = new StringBuilder();
		sb.append(m_timeFormat.format(new Date()));
		sb.append(" ");
		if(record.userToken != null)
		{
			sb.append("<");
			sb.append(record.userToken);
			sb.append(" ");
		}
		else
		{
			sb.append("< ");
		}

		switch(record.type)
		{
			case Immediate:
				sb.append("[immediate] ");
				break;

			case Exec:
				sb.append("[exec] ");
				break;

			case Notify:
				sb.append("[notify] ");
				break;

			case Status:
				sb.append("[status] ");
				break;
		}

		sb.append(record);
		sb.append("\n");
		myGdbConsole.getConsole().print(sb.toString(), ConsoleViewContentType.SYSTEM_OUTPUT);
	}

	/**
	 * Callback function for when GDB has responded to our thread information request.
	 *
	 * @param threadInfoEvent The event.
	 * @param stoppedEvent    The 'target stopped' event that caused us to make the request.
	 */
	private void onGdbThreadInfoReady(GdbEvent threadInfoEvent, GdbStoppedEvent stoppedEvent)
	{
		List<GdbThread> threads = null;

		if(threadInfoEvent instanceof GdbErrorEvent)
		{
			LOGGER.warn("Failed to get thread information: " + ((GdbErrorEvent) threadInfoEvent).message);
		}
		else if(!(threadInfoEvent instanceof GdbThreadInfo))
		{
			LOGGER.warn("Unexpected event " + threadInfoEvent + " received from -thread-info " +
					"request");
		}
		else
		{
			threads = ((GdbThreadInfo) threadInfoEvent).threads;
		}

		// Handle the event
		handleTargetStopped(stoppedEvent, threads);
	}

	/**
	 * Handles a 'target stopped' event.
	 *
	 * @param stoppedEvent The event.
	 * @param threads      Thread information, if available.
	 */
	private void handleTargetStopped(GdbStoppedEvent stoppedEvent, List<GdbThread> threads)
	{
		GdbSuspendContext suspendContext = new GdbSuspendContext(myGdb, stoppedEvent, threads);

		// Find the breakpoint if necessary
		XBreakpoint<XBreakpointProperties> breakpoint = null;
		if(stoppedEvent.reason == GdbStoppedEvent.Reason.BreakpointHit && stoppedEvent.breakpointNumber != null)
		{
			breakpoint = myBreakpointHandler.findBreakpoint(stoppedEvent.breakpointNumber);
		}

		if(breakpoint != null)
		{
			// TODO: Support log expressions
			boolean suspendProcess = getSession().breakpointReached(breakpoint, null, suspendContext);
			if(!suspendProcess)
			{
				// Resume execution
				resume();
			}
		}
		else
		{
			getSession().positionReached(suspendContext);
		}
	}
}
