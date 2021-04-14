package dev.nimbler.language.bytecode.java.reader;

import java.io.DataInput;

public interface CodeAttributeListener {

	void onCode(int maxStack, int maxLocals, int codeLength, DataInput dataInput);
	
	void onExceptionTableLength(int exceptionTableLength);
	
	void onExceptionTableEntry(int index, int startPc, int endPc, int handlerPc, int catchType);
	
}
