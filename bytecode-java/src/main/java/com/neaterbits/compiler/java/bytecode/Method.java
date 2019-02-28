package com.neaterbits.compiler.java.bytecode;

final class Method extends Member {

	private int maxLocals;
	private int maxStack;
	
	private byte [] bytecode;
	
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
		this.maxLocals = maxLocals;
		this.maxStack = maxStack;
		this.bytecode = bytecode;
	}
}
