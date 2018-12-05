package com.neaterbits.compiler.bytecode.common;

import java.util.List;

import com.neaterbits.compiler.common.FieldType;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.codemap.MethodVariant;

public interface ClassBytecode extends ClassStatics, ClassFields {

	TypeName getSuperClass();
	
	TypeVariant getTypeVariant();
	
	// Methods

	int getMethodCount();
	
	String getMethodName(int methodIdx);
	
	FieldType getMethodSignature(int methodIdx, List<FieldType> parameterTypes);

	MethodVariant getMethodVariant(int methodIdx);
	
	int getMethodIndex(String methodName, List<FieldType> parameterTypes);
	
	FieldType getMethodReturnType(int methodIdx);

	byte [] getMethodBytecode(int methodIdx);
	
	int getMethodMaxOperandStack(int methodIdx);
	
	int getMethodMaxLocals(int methodIdx);

	<T> void scanMethodClassReferences(int methodIdx, MethodClassReferenceScanner<T> scanner, T param);

}
