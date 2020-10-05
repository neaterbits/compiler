package com.neaterbits.compiler.bytecode.common.loader;

import java.util.List;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.bytecode.common.MethodType;
import com.neaterbits.compiler.util.FieldType;

final class InvokedMethod {
	private final TypeName typeName;
	private final String methodName;
	private final FieldType returnType;
	private final List<FieldType> parameterTypes;
	private final MethodType methodType;
	
	InvokedMethod(TypeName typeName, String methodName, FieldType returnType, List<FieldType> parameterTypes,
			MethodType methodType) {
		this.typeName = typeName;
		this.methodName = methodName;
		this.returnType = returnType;
		this.parameterTypes = parameterTypes;
		this.methodType = methodType;
	}

	TypeName getTypeName() {
		return typeName;
	}

	String getMethodName() {
		return methodName;
	}

	FieldType getReturnType() {
		return returnType;
	}

	List<FieldType> getParameterTypes() {
		return parameterTypes;
	}

	MethodType getMethodType() {
		return methodType;
	}
}
