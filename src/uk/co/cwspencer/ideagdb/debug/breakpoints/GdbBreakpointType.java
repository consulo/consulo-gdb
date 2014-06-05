package uk.co.cwspencer.ideagdb.debug.breakpoints;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.gdb.GdbSupportProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase;
import uk.co.cwspencer.ideagdb.debug.GdbDebuggerEditorsProvider;

public class GdbBreakpointType extends XLineBreakpointTypeBase
{
	public GdbBreakpointType()
	{
		super("gdb", "GDB Breakpoints", new GdbDebuggerEditorsProvider());
	}

	@Override
	public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project)
	{
		FileType fileType = file.getFileType();
		for(GdbSupportProvider gdbSupportProvider : GdbSupportProvider.EP_NAME.getExtensions())
		{
			if(ArrayUtil.contains(fileType, gdbSupportProvider.getApplicableFileTypes()))
			{
				return true;
			}
		}
		return false;
	}
}
