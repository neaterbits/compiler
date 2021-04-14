package dev.nimbler.exe.vm.bytecode.loader;


public final class IntegerTypeMap extends HashTypeMap<Integer> {

	public IntegerTypeMap() {
		super(Integer::intValue, integer -> null);
	}
}
