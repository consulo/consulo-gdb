package uk.co.cwspencer.ideagdb.debug;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.UnknownFileType;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.evaluation.EvaluationMode;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;

public class GdbDebuggerEditorsProvider extends XDebuggerEditorsProvider
{
	@NotNull
	@Override
	public FileType getFileType()
	{
		// TODO: Return a proper value
		return UnknownFileType.INSTANCE;
	}

	@NotNull
	@Override
	public Document createDocument(@NotNull Project project, @NotNull String text,
		@Nullable XSourcePosition sourcePosition, @NotNull EvaluationMode mode)
	{
		// TODO: Return a proper value
		return EditorFactory.getInstance().createDocument(text);
	}
}
