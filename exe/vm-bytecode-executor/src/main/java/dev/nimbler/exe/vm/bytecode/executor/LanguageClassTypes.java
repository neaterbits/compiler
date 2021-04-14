package dev.nimbler.exe.vm.bytecode.executor;

import java.util.List;

import dev.nimbler.language.bytecode.common.ClassMethod;
import dev.nimbler.language.common.types.FieldType;

public interface LanguageClassTypes {

	FieldType getByteType();
	FieldType getIntType();
	FieldType getLongType();
	FieldType getStringType();

	List<ClassMethod> getClassTypeMethods();
	
	List<ClassMethod> getBaseTypeMethods();
	
}
