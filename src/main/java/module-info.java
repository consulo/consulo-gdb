/**
 * @author VISTALL
 * @since 13-Aug-22
 */
module consulo.gdb {
	requires consulo.execution.debug.api;

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