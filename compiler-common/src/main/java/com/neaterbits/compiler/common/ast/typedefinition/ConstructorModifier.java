package com.neaterbits.compiler.common.ast.typedefinition;

public interface ConstructorModifier {

	<T, R> R visit(ConstructorModifierVisitor<T, R> visitor, T param);
	
}
