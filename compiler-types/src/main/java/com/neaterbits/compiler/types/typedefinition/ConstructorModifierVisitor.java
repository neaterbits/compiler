package com.neaterbits.compiler.types.typedefinition;

public interface ConstructorModifierVisitor<T, R> {

	R onVisibility(ConstructorVisibility visibility, T param);
	
}
