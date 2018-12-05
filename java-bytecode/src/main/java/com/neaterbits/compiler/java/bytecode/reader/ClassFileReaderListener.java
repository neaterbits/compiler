package com.neaterbits.compiler.java.bytecode.reader;

import java.io.DataInput;
import java.io.IOException;

public interface ClassFileReaderListener {

	void onConstantPoolCount(int count);
	
	void onConstantPoolClass(int index, int nameIndex);
	
	void onConstantPoolFieldref(int index, int classIndex, int nameAndTypeIndex);
	void onConstantPoolMethodref(int index, int classIndex, int nameAndTypeIndex);
	void onConstantPoolInterfaceMethodref(int index, int classIndex, int nameAndTypeIndex);
	
	void onConstantPoolString(int index, int stringIndex);
	
	void onConstantPoolInteger(int index, int value);
	void onConstantPoolFloat(int index, float value);
	void onConstantPoolLong(int index, long value);
	void onConstantPoolDouble(int index, double value);
	
	void onConstantPoolNameAndType(int index, int nameIndex, int descriptorIndex);

	void onConstantPoolUTF8(int index, String value);
	
	void onConstantPoolMethodHandle(int index, int referenceKind, int referenceIndex);
	void onConstantPoolMethodType(int index, int descriptorIndex);
	
	void onConstantPoolInvokeDynamic(int index, int bootstrapMethodAttrIndex, int nameAndTypeIndex);

	void onClassInfo(int accessFlags, int thisClass, int superClass);
	
	void onInterfacesCount(int count);
	void onInterface(int index, int nameIndex);

	void onFieldCount(int count);
	void onField(int index, int accessFlags, int nameIndex, int descriptorIndex, int attributesCount);
	
	default void onFieldAttribute(int fieldIndex, int attributeIndex, int attributeNameIndex, int attributeLength, DataInput dataInput) throws IOException {
		dataInput.skipBytes(attributeLength);
	}
	
	void onMethodCount(int count);
	void onMethod(int index, int accessFlags, int nameIndex, int descriptorIndex, int attributesCount);
	
	default void onMethodAttribute(int methodIndex, int attributeIndex, int attributeNameIndex, int attributeLength, DataInput dataInput) throws IOException {
		dataInput.skipBytes(attributeLength);
	}

	void onClassFileAttributeCount(int count);
	default void onClassFileAttribute(int attributeIndex, int attributeNameIndex, int attributeLength, DataInput dataInput) throws IOException {
		dataInput.skipBytes(attributeLength);
	}
}
