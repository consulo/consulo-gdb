package uk.co.cwspencer.ideagdb.run;

import consulo.execution.DefaultExecutionResult;
import consulo.execution.ExecutionResult;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.debug.DefaultDebugProcessHandler;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ProgramRunner;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdbRunProfileState implements RunProfileState
{
	private Project myProject;

	public GdbRunProfileState(Project project)
	{
		myProject = project;
	}

	@Nullable
	@Override
	public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException
	{
		ProcessHandler processHandler = new DefaultDebugProcessHandler();

		// Create the console
		final TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(myProject);

		return new DefaultExecutionResult(builder.getConsole(), processHandler);
	}
}
