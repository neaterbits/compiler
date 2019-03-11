package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.util.TypeName;

@FunctionalInterface
public interface ASTMethodVisitor {

	void onMethod(String name, MethodVariant methodVariant, TypeName returnType, TypeName [] parameterTypes, int indexInType);
	
}
