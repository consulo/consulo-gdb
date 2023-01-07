package uk.co.cwspencer.ideagdb.run;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.ExecutionResult;
import consulo.execution.configuration.RunProfile;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.debug.*;
import consulo.execution.runner.DefaultProgramRunner;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.RunContentDescriptor;
import consulo.process.ExecutionException;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.cwspencer.ideagdb.debug.GdbDebugProcess;

@ExtensionImpl(order = "last")
public class GdbRunner extends DefaultProgramRunner
{
	@NotNull
	@Override
	public String getRunnerId()
	{
		return "GdbRunner";
	}

	@Override
	public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile)
	{
		return DefaultDebugExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof GdbRunConfiguration;
	}

	@Override
	@Nullable
	protected RunContentDescriptor doExecute(@NotNull final RunProfileState state, @NotNull final ExecutionEnvironment env) throws ExecutionException
	{
		Project project = env.getProject();
		final XDebugSession debugSession = XDebuggerManager.getInstance(project).startSession(env, new XDebugProcessStarter()
		{
			@NotNull
			@Override
			public XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException
			{
				final ExecutionResult result = state.execute(env.getExecutor(), GdbRunner.this);
				assert result != null;
				return new GdbDebugProcess(session, env, result.getExecutionConsole());
			}
		});
		return debugSession.getRunContentDescriptor();
	}
}
