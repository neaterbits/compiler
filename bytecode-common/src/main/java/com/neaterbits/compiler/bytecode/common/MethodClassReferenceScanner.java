package com.neaterbits.compiler.bytecode.common;

import java.util.List;

import com.neaterbits.compiler.util.FieldType;
import com.neaterbits.compiler.util.TypeName;

public interface MethodClassReferenceScanner<T> {

	void onInstanceCreation(T param, TypeName typeName);
	
	void onMethodInvocation(
			T param,
			TypeName typeName,
			String methodName,
			FieldType returnType,
			List<FieldType> parameterTypes,
			MethodType methodType);
}
