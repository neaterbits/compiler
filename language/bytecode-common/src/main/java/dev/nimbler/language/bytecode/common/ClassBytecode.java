package dev.nimbler.language.bytecode.common;

import java.util.List;

import dev.nimbler.language.common.types.FieldType;
import dev.nimbler.language.common.types.MethodVariant;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypeVariant;

public interface ClassBytecode extends ClassStatics, ClassFields {

    TypeName getTypeName();

	TypeName getSuperClass();
	
	int getImplementedInterfacesCount();
	
	TypeName getImplementedInterface(int interfaceIdx);
	
	TypeVariant getTypeVariant();
	
	// Methods

	int getMethodCount();
	
	String getMethodName(int methodIdx);
	
	FieldType getMethodSignature(int methodIdx, List<FieldType> parameterTypes);

	int getMethodParameterCount(int methodIdx);

	MethodVariant getMethodVariant(int methodIdx);
	
	int getMethodIndex(String methodName, List<FieldType> parameterTypes);
	
	FieldType getMethodReturnType(int methodIdx);

	byte [] getMethodBytecode(int methodIdx);
	
	int getMethodMaxOperandStack(int methodIdx);
	
	int getMethodMaxLocals(int methodIdx);

	<T> void scanMethodClassReferences(int methodIdx, MethodClassReferenceScanner<T> scanner, T param);

}
