package uk.co.cwspencer.ideagdb.run;

import java.io.File;
import java.util.Collection;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RunConfigurationWithSuppressedDefaultDebugAction;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

public class GdbRunConfiguration extends ModuleBasedConfiguration<RunConfigurationModule>
	implements RunConfigurationWithSuppressedDefaultRunAction,
	RunConfigurationWithSuppressedDefaultDebugAction
{
	private static final Logger m_log =
		Logger.getInstance("#uk.co.cwspencer.ideagdb.run.GdbRunConfiguration");

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
	public Collection<Module> getValidModules()
	{
		m_log.warn("getValidModules: stub");
		return null;
	}

	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
	{
		return new GdbRunConfigurationEditor<GdbRunConfiguration>(getProject());
	}

	@Nullable
	@Override
	public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env)
		throws ExecutionException
	{
		return new GdbRunProfileState(getProject());
	}

	@Override
	public void readExternal(Element element) throws InvalidDataException
	{
		super.readExternal(element);
		readModule(element);
		DefaultJDOMExternalizer.readExternal(this, element);
	}

	@Override
	public void writeExternal(Element element) throws WriteExternalException
	{
		super.writeExternal(element);
		writeModule(element);
		DefaultJDOMExternalizer.writeExternal(this, element);
	}
}
