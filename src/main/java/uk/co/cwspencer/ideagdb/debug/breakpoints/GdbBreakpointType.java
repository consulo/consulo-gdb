package uk.co.cwspencer.ideagdb.debug.breakpoints;

import org.jetbrains.annotations.NotNull;
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase;
import uk.co.cwspencer.ideagdb.debug.GdbDebuggerEditorsProvider;

public class GdbBreakpointType extends XLineBreakpointTypeBase
{
	@NotNull
	public static GdbBreakpointType getInstance()
	{
		return EXTENSION_POINT_NAME.findExtension(GdbBreakpointType.class);
	}

	public GdbBreakpointType()
	{
		super("gdb-line-breakpoint", "GDB Line Breakpoints", new GdbDebuggerEditorsProvider());
	}
}
