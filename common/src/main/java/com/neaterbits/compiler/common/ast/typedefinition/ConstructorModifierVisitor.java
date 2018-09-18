package com.neaterbits.compiler.common.ast.typedefinition;

public interface ConstructorModifierVisitor<T, R> {

	R onVisibility(ConstructorVisibility visibility, T param);
	
}
