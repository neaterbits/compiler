package dev.nimbler.language.bytecode.java.reader;

import java.util.Arrays;

public enum ConstantPoolTag {

	CLASS(7),
	FIELDREF(9),
	METHODREF(10),
	INTERFACE_METHODREF(11),
	STRING(8),
	INTEGER(3),
	FLOAT(4),
	LONG(5),
	DOUBLE(6),
	NAME_AND_TYPE(12),
	UTF8(1),
	METHOD_HANDLE(15),
	METHOD_TYPE(16),
	INVOKE_DYNAMIC(18);

	private final int value;

	private static final ConstantPoolTag [] byValue;
	
	static {
		
		final int arraySize = Arrays.stream(values())
				.max((cpt1, cpt2) -> Integer.compare(cpt1.value, cpt2.value))
				.get()
				.value + 1;
		
		byValue = new ConstantPoolTag[arraySize];

		for (ConstantPoolTag cpt : values()) {
			byValue[cpt.value] = cpt;
		}
	}
	
	public static ConstantPoolTag getConstantPoolTag(int value) {
		return byValue[value];
	}
	
	private ConstantPoolTag(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
