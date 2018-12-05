package com.neaterbits.compiler.bytecode.common.loader;

import java.util.List;

import com.neaterbits.compiler.common.FieldType;
import com.neaterbits.compiler.common.TypeName;

abstract class NameLoadMethodRequest extends LoadMethodRequest {

	private final TypeName typeName;
	private final String methodName;
	private final List<FieldType> parameterTypes;
	
	abstract int getCallingMethod();
	
	NameLoadMethodRequest(TypeName typeName, String methodName, List<FieldType> parameterTypes) {
		this.typeName = typeName;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
	}

	final TypeName getTypeName() {
		return typeName;
	}

	final String getMethodName() {
		return methodName;
	}

	final List<FieldType> getParameterTypes() {
		return parameterTypes;
	}
}
