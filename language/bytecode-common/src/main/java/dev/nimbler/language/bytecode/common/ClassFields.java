package dev.nimbler.language.bytecode.common;

import dev.nimbler.language.common.types.FieldType;

public interface ClassFields {

	// Fields
	int getFieldCount();
	
	String getFieldName(int fieldIdx);
	
	FieldType getFieldType(int fieldIdx);
	
	boolean isStatic(int fieldIdx);

}
