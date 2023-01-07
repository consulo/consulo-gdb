package uk.co.cwspencer.ideagdb.debug;

import consulo.codeEditor.EditorFactory;
import consulo.document.Document;
import consulo.execution.debug.XSourcePosition;
import consulo.execution.debug.evaluation.EvaluationMode;
import consulo.execution.debug.evaluation.XDebuggerEditorsProvider;
import consulo.project.Project;
import consulo.virtualFileSystem.fileType.FileType;
import consulo.virtualFileSystem.fileType.UnknownFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
