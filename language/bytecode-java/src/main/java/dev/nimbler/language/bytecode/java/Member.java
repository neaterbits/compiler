package dev.nimbler.language.bytecode.java;

public abstract class Member {

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

	public final int getNameIndex() {
		return nameIndex;
	}

	public final int getDescriptorIndex() {
		return descriptorIndex;
	}

	int getAttributesCount() {
		return attributesCount;
	}
}
