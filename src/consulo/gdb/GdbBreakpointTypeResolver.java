package consulo.gdb;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import consulo.xdebugger.breakpoints.XLineBreakpointTypeResolver;
import uk.co.cwspencer.ideagdb.debug.breakpoints.GdbBreakpointType;

/**
 * @author VISTALL
 * @since 5/8/2016
 */
public class GdbBreakpointTypeResolver implements XLineBreakpointTypeResolver
{
	@Nullable
	@Override
	public XLineBreakpointType<?> resolveBreakpointType(@NotNull Project project, @NotNull VirtualFile virtualFile, int line)
	{
		return GdbBreakpointType.getInstance();
	}
}
