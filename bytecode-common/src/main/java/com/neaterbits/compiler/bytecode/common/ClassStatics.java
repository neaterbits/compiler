package com.neaterbits.compiler.bytecode.common;

import com.neaterbits.compiler.common.FieldType;

public interface ClassStatics {

	int getStaticFieldCount();
	
	FieldType getStaticFieldType(int index);
	
	int getStaticMethodCount();
}
