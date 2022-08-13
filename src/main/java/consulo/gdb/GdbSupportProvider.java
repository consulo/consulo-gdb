package consulo.gdb;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.component.extension.ExtensionPointName;
import consulo.module.extension.ModuleExtension;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 05.06.14
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public abstract class GdbSupportProvider
{
	public static final ExtensionPointName<GdbSupportProvider> EP_NAME = ExtensionPointName.create(GdbSupportProvider.class);

	private final Class<ModuleExtension<?>> myApplicableModuleExtension;

	public GdbSupportProvider(Class<ModuleExtension<?>> extension)
	{
		myApplicableModuleExtension = extension;
	}

	@NotNull
	public Class<ModuleExtension<?>> getApplicableModuleExtension()
	{
		return myApplicableModuleExtension;
	}
}
