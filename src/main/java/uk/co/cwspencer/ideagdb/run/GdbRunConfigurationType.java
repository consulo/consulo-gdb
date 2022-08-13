package uk.co.cwspencer.ideagdb.run;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.AllIcons;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationType;
import consulo.execution.configuration.ConfigurationTypeUtil;
import consulo.execution.configuration.RunConfiguration;
import consulo.gdb.GdbSupportProvider;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.project.Project;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
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
	public Image getIcon()
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
