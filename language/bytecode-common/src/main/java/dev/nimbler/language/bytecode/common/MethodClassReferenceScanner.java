package dev.nimbler.language.bytecode.common;

import java.util.List;

import dev.nimbler.language.common.types.FieldType;
import dev.nimbler.language.common.types.TypeName;

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
