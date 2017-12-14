package consulo.gdb;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.extensions.ExtensionPointName;
import consulo.module.extension.ModuleExtension;

/**
 * @author VISTALL
 * @since 05.06.14
 */
public abstract class GdbSupportProvider
{
	public static final ExtensionPointName<GdbSupportProvider> EP_NAME = ExtensionPointName.create("consulo.gdb.supportProvider");

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
