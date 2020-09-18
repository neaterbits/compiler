package com.neaterbits.compiler.model.common;

import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.util.TypeName;

@FunctionalInterface
public interface MethodVisitor {

	void onMethod(String name, MethodVariant methodVariant, TypeName returnType, TypeName [] parameterTypes, int indexInType);
	
}
