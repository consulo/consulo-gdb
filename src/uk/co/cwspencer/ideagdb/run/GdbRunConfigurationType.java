package uk.co.cwspencer.ideagdb.run;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.gdb.GdbSupportProvider;
import org.mustbe.consulo.module.extension.ModuleExtensionHelper;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;

public class GdbRunConfigurationType implements ConfigurationType
{
	@NotNull
	public static GdbRunConfigurationType getInstance()
	{
		return ConfigurationTypeUtil.findConfigurationType(GdbRunConfigurationType.class);
	}

	private final ConfigurationFactory myFactory = new ConfigurationFactory(this)
	{
		@Override
		public RunConfiguration createTemplateConfiguration(Project project)
		{
			return new GdbRunConfiguration("Unnamed", project, this);
		}

		@Override
		public boolean isApplicable(@NotNull Project project)
		{
			for(GdbSupportProvider gdbSupportProvider : GdbSupportProvider.EP_NAME.getExtensions())
			{
				if(ModuleExtensionHelper.getInstance(project).hasModuleExtension(gdbSupportProvider.getApplicableModuleExtension()))
				{
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean canConfigurationBeSingleton()
		{
			return false;
		}
	};

	@Override
	public String getDisplayName()
	{
		return "GDB";
	}

	@Override
	public String getConfigurationTypeDescription()
	{
		return "GDB debug configuration";
	}

	@Override
	public Icon getIcon()
	{
		return AllIcons.RunConfigurations.Application;
	}

	@NotNull
	@Override
	public String getId()
	{
		return "GdbRunConfigurationType";
	}

	@Override
	public ConfigurationFactory[] getConfigurationFactories()
	{
		return new ConfigurationFactory[]{myFactory};
	}
}
