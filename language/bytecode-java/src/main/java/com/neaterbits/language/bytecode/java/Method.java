package com.neaterbits.language.bytecode.java;

import java.io.DataInput;
import java.io.IOException;
import java.util.Objects;

public final class Method extends Member {

	private int maxLocals;
	private int maxStack;
	
	private byte [] bytecode;
	private long [] exceptionTable;
	
	Method(int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
		super(accessFlags, nameIndex, descriptorIndex, attributesCount);
	}

	int getMaxLocals() {
		return maxLocals;
	}

	int getMaxStack() {
		return maxStack;
	}

	byte[] getBytecode() {
		return bytecode;
	}

	void setBytecode(int maxLocals, int maxStack, byte[] bytecode) {
	
		Objects.requireNonNull(bytecode);
		
		if (this.bytecode != null) {
			throw new IllegalStateException();
		}
		
		this.maxLocals = maxLocals;
		this.maxStack = maxStack;
		this.bytecode = bytecode;
	}
	
	void setExceptionTable(long [] exceptionTable) {
		
		Objects.requireNonNull(exceptionTable);

		if (this.exceptionTable != null) {
			throw new IllegalStateException();
		}
		
		this.exceptionTable = exceptionTable;
	}

	void setCodeAttributesCount(int codeAttributesCount) {
		
	}
	
	void onCodeAttribute(int codeAttributeIndex, int codeAttributeNameIndex, int codeAttributeLength, DataInput dataInput) throws IOException {
		dataInput.skipBytes(codeAttributeLength);
	}
}
