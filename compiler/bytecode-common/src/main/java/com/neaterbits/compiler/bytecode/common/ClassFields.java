package com.neaterbits.compiler.bytecode.common;

import com.neaterbits.compiler.util.FieldType;

public interface ClassFields {

	// Fields
	int getFieldCount();
	
	String getFieldName(int fieldIdx);
	
	FieldType getFieldType(int fieldIdx);
	
	boolean isStatic(int fieldIdx);

}
