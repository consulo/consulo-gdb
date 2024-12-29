package uk.co.cwspencer.ideagdb.debug.breakpoints;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.debug.breakpoint.XLineBreakpointTypeBase;
import uk.co.cwspencer.ideagdb.debug.GdbDebuggerEditorsProvider;

import jakarta.annotation.Nonnull;

@ExtensionImpl
public class GdbBreakpointType extends XLineBreakpointTypeBase
{
	@Nonnull
	public static GdbBreakpointType getInstance()
	{
		return EXTENSION_POINT_NAME.findExtensionOrFail(GdbBreakpointType.class);
	}

	public GdbBreakpointType()
	{
		super("gdb-line-breakpoint", "GDB Line Breakpoints", new GdbDebuggerEditorsProvider());
	}
}
