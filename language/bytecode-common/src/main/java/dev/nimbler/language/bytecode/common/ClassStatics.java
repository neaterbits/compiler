package dev.nimbler.language.bytecode.common;

import dev.nimbler.language.common.types.FieldType;

public interface ClassStatics {

	int getStaticFieldCount();
	
	FieldType getStaticFieldType(int index);
	
	int getStaticMethodCount();
}
