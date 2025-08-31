package uk.co.cwspencer.ideagdb.run;

import consulo.application.AllIcons;
import consulo.configurable.ConfigurationException;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.localize.LocalizeValue;
import consulo.process.cmd.ParametersListUtil;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.TextBoxWithExpandAction;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.util.FormBuilder;
import jakarta.annotation.Nullable;

public class GdbRunConfigurationEditor<T extends GdbRunConfiguration> extends SettingsEditor<T>
{
	private FileChooserTextBoxBuilder.Controller myGdbPathController;
	private FileChooserTextBoxBuilder.Controller myAppPathController;
	private TextBoxWithExpandAction myStartupParameters;

	private Component myRootComponent;

	@RequiredUIAccess
	public GdbRunConfigurationEditor(final Project project)
	{
		FormBuilder builder = FormBuilder.create();

		myGdbPathController = FileChooserTextBoxBuilder.create(project).fileChooserDescriptor(FileChooserDescriptorFactory.createSingleLocalFileDescriptor()).build();
		builder.addLabeled(LocalizeValue.localizeTODO("&GDB executable:"), myGdbPathController.getComponent());

		myAppPathController = FileChooserTextBoxBuilder.create(project).fileChooserDescriptor(FileChooserDescriptorFactory.createSingleLocalFileDescriptor()).build();
		builder.addLabeled(LocalizeValue.localizeTODO("&Application executable:"), myAppPathController.getComponent());

		builder.addLabeled(LocalizeValue.localizeTODO("&Startup commands:"), myStartupParameters = TextBoxWithExpandAction.create(AllIcons.Actions.ShowViewer, "", ParametersListUtil
				.DEFAULT_LINE_PARSER, ParametersListUtil.DEFAULT_LINE_JOINER));

		myRootComponent = builder.build();
	}

	@Override
	@RequiredUIAccess
	protected void resetEditorFrom(T configuration)
	{
		myGdbPathController.setValue(configuration.GDB_PATH);
		myAppPathController.setValue(configuration.APP_PATH);
		myStartupParameters.setValue(configuration.STARTUP_COMMANDS);
	}

	@Override
	@RequiredUIAccess
	protected void applyEditorTo(T configuration) throws ConfigurationException
	{
		configuration.GDB_PATH = myGdbPathController.getValue();
		configuration.APP_PATH = myAppPathController.getValue();
		configuration.STARTUP_COMMANDS = myStartupParameters.getValue();
	}

	@RequiredUIAccess
	@Nullable
	@Override
	protected Component createUIComponent()
	{
		return myRootComponent;
	}
}
