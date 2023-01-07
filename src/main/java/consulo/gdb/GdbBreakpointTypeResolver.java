package consulo.gdb;

import consulo.annotation.access.RequiredReadAction;
import consulo.execution.debug.breakpoint.XLineBreakpointType;
import consulo.execution.debug.breakpoint.XLineBreakpointTypeResolver;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import uk.co.cwspencer.ideagdb.debug.breakpoints.GdbBreakpointType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 5/8/2016
 */
public abstract class GdbBreakpointTypeResolver implements XLineBreakpointTypeResolver
{
	@RequiredReadAction
	@Nullable
	@Override
	public XLineBreakpointType<?> resolveBreakpointType(@Nonnull Project project, @Nonnull VirtualFile virtualFile, int line)
	{
		return GdbBreakpointType.getInstance();
	}
}
