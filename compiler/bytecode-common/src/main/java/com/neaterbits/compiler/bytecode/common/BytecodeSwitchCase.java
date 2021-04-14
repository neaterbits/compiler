package com.neaterbits.compiler.bytecode.common;

public abstract class BytecodeSwitchCase {

	public abstract int switchCase(byte [] bytecodes, int index, BytecodeInstructions listener);
	
}
