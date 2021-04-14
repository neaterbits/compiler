package com.neaterbits.language.bytecode.common;

import com.neaterbits.language.common.types.FieldType;

public interface ClassStatics {

	int getStaticFieldCount();
	
	FieldType getStaticFieldType(int index);
	
	int getStaticMethodCount();
}
