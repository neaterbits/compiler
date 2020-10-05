package com.neaterbits.compiler.bytecode.common;

import java.util.List;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.util.FieldType;

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
