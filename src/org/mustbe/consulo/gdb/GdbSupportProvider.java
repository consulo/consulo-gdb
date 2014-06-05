package org.mustbe.consulo.gdb;

import org.consulo.module.extension.ModuleExtension;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.fileTypes.FileType;

/**
 * @author VISTALL
 * @since 05.06.14
 */
public abstract class GdbSupportProvider
{
	public static final ExtensionPointName<GdbSupportProvider> EP_NAME = ExtensionPointName.create("org.mustbe.consulo.gdb.supportProvider");

	private final FileType[] myApplicableFileTypes;
	private final Class<ModuleExtension<?>> myApplicableModuleExtension;

	public GdbSupportProvider(Class<ModuleExtension<?>> extension, FileType... applicableFileTypes)
	{
		myApplicableModuleExtension = extension;
		myApplicableFileTypes = applicableFileTypes;
	}

	@NotNull
	public FileType[] getApplicableFileTypes()
	{
		return myApplicableFileTypes;
	}

	@NotNull
	public Class<ModuleExtension<?>> getApplicableModuleExtension()
	{
		return myApplicableModuleExtension;
	}
}
