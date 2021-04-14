package com.neaterbits.language.bytecode.common;

import java.util.List;

import com.neaterbits.language.common.types.FieldType;
import com.neaterbits.language.common.types.TypeName;

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
