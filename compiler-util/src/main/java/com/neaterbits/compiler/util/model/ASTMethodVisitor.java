package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.TypeName;

@FunctionalInterface
public interface ASTMethodVisitor {

	void onMethod(String name, MethodVariant methodVariant, TypeName returnType, TypeName [] parameterTypes, int indexInType);
	
}
