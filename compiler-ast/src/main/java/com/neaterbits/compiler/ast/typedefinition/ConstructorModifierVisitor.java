package com.neaterbits.compiler.ast.typedefinition;

public interface ConstructorModifierVisitor<T, R> {

	R onVisibility(ConstructorVisibility visibility, T param);
	
}
