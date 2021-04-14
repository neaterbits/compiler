package com.neaterbits.language.bytecode.java.reader;

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
	
	void onDeprecated(int memberIndex);
	
	void onAnnotationDefault(int memberIndex, int attributeLength, DataInput dataInput) throws IOException;

	void onRuntimeInvisibleAnnotations(int memberIndex, int attributeLength, DataInput dataInput) throws IOException;

	void onRuntimeInvisibleParameterAnnotations(int memberIndex, int attributeLength, DataInput dataInput) throws IOException;

	void onSynthetic(int memberIndex);
}
