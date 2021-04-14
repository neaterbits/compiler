package com.neaterbits.compiler.bytecode.common.loader;


public final class IntegerTypeMap extends HashTypeMap<Integer> {

	public IntegerTypeMap() {
		super(Integer::intValue, integer -> null);
	}
}
