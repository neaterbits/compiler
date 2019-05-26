package com.neaterbits.compiler.util.typedefinition;

public interface ConstructorModifierVisitor<T, R> {

	R onVisibility(ConstructorVisibility visibility, T param);
	
}
