package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.MethodVariant;

@FunctionalInterface
public interface ASTMethodVisitor {

	void onMethod(String name, MethodVariant methodVariant, TypeName returnType, TypeName [] parameterTypes, int indexInType);
	
}
