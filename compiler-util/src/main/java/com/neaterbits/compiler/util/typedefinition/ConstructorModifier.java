package com.neaterbits.compiler.util.typedefinition;

public interface ConstructorModifier {

	<T, R> R visit(ConstructorModifierVisitor<T, R> visitor, T param);
	
}
