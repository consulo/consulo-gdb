/**
 * @author VISTALL
 * @since 13-Aug-22
 */
module consulo.gdb {
	requires consulo.execution.debug.api;

	requires consulo.application.api;
	requires consulo.code.editor.api;
	requires consulo.component.api;
	requires consulo.configurable.api;
	requires consulo.document.api;
	requires consulo.execution.api;
	requires consulo.execution.impl;
	requires consulo.file.chooser.api;
	requires consulo.localize.api;
	requires consulo.logging.api;
	requires consulo.module.api;
	requires consulo.process.api;
	requires consulo.project.api;
	requires consulo.ui.api;
	requires consulo.ui.ex.api;
	requires consulo.virtual.file.system.api;
	requires consulo.util.collection;
	requires consulo.util.xml.serializer;

	// TODO remove in future
	requires java.desktop;

	exports consulo.gdb;
	exports uk.co.cwspencer.gdb;
	exports uk.co.cwspencer.gdb.gdbmi;
	exports uk.co.cwspencer.gdb.messages;
	exports uk.co.cwspencer.gdb.messages.annotations;
	exports uk.co.cwspencer.ideagdb.debug;
	exports uk.co.cwspencer.ideagdb.debug.breakpoints;
	exports uk.co.cwspencer.ideagdb.run;
}