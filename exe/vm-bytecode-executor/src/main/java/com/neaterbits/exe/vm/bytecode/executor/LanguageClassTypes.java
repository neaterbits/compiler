package com.neaterbits.exe.vm.bytecode.executor;

import java.util.List;

import com.neaterbits.language.bytecode.common.ClassMethod;
import com.neaterbits.language.common.types.FieldType;

public interface LanguageClassTypes {

	FieldType getByteType();
	FieldType getIntType();
	FieldType getLongType();
	FieldType getStringType();

	List<ClassMethod> getClassTypeMethods();
	
	List<ClassMethod> getBaseTypeMethods();
	
}
