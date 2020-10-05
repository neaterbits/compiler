package com.neaterbits.compiler.model.common;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.types.MethodVariant;

@FunctionalInterface
public interface MethodVisitor {

	void onMethod(String name, MethodVariant methodVariant, TypeName returnType, TypeName [] parameterTypes, int indexInType);
	
}
