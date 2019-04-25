package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.TypeName;

@FunctionalInterface
public interface MethodVisitor {

	void onMethod(String name, MethodVariant methodVariant, TypeName returnType, TypeName [] parameterTypes, int indexInType);
	
}
