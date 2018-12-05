package com.neaterbits.compiler.bytecode.common.executor;

import java.util.List;

import com.neaterbits.compiler.bytecode.common.ClassMethod;
import com.neaterbits.compiler.common.FieldType;

public interface LanguageClassTypes {

	FieldType getByteType();
	FieldType getIntType();
	FieldType getLongType();
	FieldType getStringType();

	List<ClassMethod> getClassTypeMethods();
	
	List<ClassMethod> getBaseTypeMethods();
	
}
