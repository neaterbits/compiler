package com.neaterbits.compiler.java.bytecode.reader;

import java.io.DataInput;
import java.io.IOException;

public interface ClassFileAttributesListener {

	void onConstantValue(int memberIndex, int constantValueIndex);
	
	void onCode(int memberIndex, int attributeLength, DataInput dataInput) throws IOException;

	void onStackMapTable(int memberIndex, int attributeLength, DataInput dataInput) throws IOException;
	
	void onExceptions(int memberIndex, int attributeLength, DataInput dataInput) throws IOException;

	void onLocalVariableTable(int memberIndex, int attributeLength, DataInput dataInput) throws IOException;

	void onRuntimeVisibleAnnotations(int memberIndex, int attributeLength, DataInput dataInput) throws IOException;

	void onSignature(int memberIndex, int signatureIndex);
}
