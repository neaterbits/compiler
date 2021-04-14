package dev.nimbler.language.bytecode.java;

public final class Field extends Member {

	Field(int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
		super(accessFlags, nameIndex, descriptorIndex, attributesCount);
	}
}
