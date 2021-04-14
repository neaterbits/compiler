package dev.nimbler.language.bytecode.common;

public abstract class BytecodeSwitchCase {

	public abstract int switchCase(byte [] bytecodes, int index, BytecodeInstructions listener);
	
}
