package com.neaterbits.compiler.java.bytecode;

abstract class Member {

	private final int accessFlags;
	private final int nameIndex;
	private final int descriptorIndex;
	private final int attributesCount;

	Member(int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
		this.accessFlags = accessFlags;
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
		this.attributesCount = attributesCount;
	}

	int getAccessFlags() {
		return accessFlags;
	}

	int getNameIndex() {
		return nameIndex;
	}

	int getDescriptorIndex() {
		return descriptorIndex;
	}

	int getAttributesCount() {
		return attributesCount;
	}
}
