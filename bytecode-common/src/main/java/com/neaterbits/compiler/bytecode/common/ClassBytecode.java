package com.neaterbits.compiler.bytecode.common;

import java.util.List;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.util.FieldType;

public interface ClassBytecode extends ClassStatics, ClassFields {

	TypeName getSuperClass();
	
	int getImplementedInterfacesCount();
	
	TypeName getImplementedInterface(int interfaceIdx);
	
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
