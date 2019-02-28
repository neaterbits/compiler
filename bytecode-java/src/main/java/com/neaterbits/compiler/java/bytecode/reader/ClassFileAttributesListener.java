package com.neaterbits.compiler.java.bytecode.reader;

import java.io.DataInput;

public interface ClassFileAttributesListener {

	void onConstantValue(int memberIndex, int constantValueIndex);
	
	void onCode(int memberIndex, int attributeLength, DataInput dataInput);

	void onStackMapTable(int memberIndex, int attributeLength, DataInput dataInput);
	
	void onExceptions(int memberIndex, int attributeLength, DataInput dataInput);

}
