package uk.co.cwspencer.ideagdb.run;

import consulo.annotation.access.RequiredReadAction;
import consulo.execution.configuration.*;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.process.ExecutionException;
import consulo.project.Project;
import consulo.util.xml.serializer.DefaultJDOMExternalizer;
import consulo.util.xml.serializer.InvalidDataException;
import consulo.util.xml.serializer.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class GdbRunConfiguration extends ModuleBasedConfiguration<RunConfigurationModule> implements RunConfigurationWithSuppressedDefaultRunAction
{
	private static final Logger m_log = Logger.getInstance(GdbRunConfiguration.class);

	public String GDB_PATH = "gdb";
	public String APP_PATH = "";
	public String STARTUP_COMMANDS = "";

	public GdbRunConfiguration(String name, Project project, ConfigurationFactory factory)
	{
		super(name, new RunConfigurationModule(project), factory);
	}

	@Nullable
	public String getWorkingDirectory()
	{
		return new File(APP_PATH).getParent(); //TODO [VISTALL] configurable
	}

	@Override
	@RequiredReadAction
	public Collection<Module> getValidModules()
	{
		return List.of(ModuleManager.getInstance(getProject()).getModules());
	}

	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
	{
		return new GdbRunConfigurationEditor<GdbRunConfiguration>(getProject());
	}

	@Nullable
	@Override
	public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException
	{
		return new GdbRunProfileState(getProject());
	}

	@Override
	public void readExternal(Element element) throws InvalidDataException
	{
		super.readExternal(element);
		DefaultJDOMExternalizer.readExternal(this, element);
	}

	@Override
	public void writeExternal(Element element) throws WriteExternalException
	{
		super.writeExternal(element);
		DefaultJDOMExternalizer.writeExternal(this, element);
	}
}
