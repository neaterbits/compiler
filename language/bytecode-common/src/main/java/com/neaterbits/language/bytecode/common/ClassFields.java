package com.neaterbits.language.bytecode.common;

import com.neaterbits.language.common.types.FieldType;

public interface ClassFields {

	// Fields
	int getFieldCount();
	
	String getFieldName(int fieldIdx);
	
	FieldType getFieldType(int fieldIdx);
	
	boolean isStatic(int fieldIdx);

}
