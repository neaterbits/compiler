package com.neaterbits.compiler.ast.typedefinition;

public interface FieldModifier {

	<T, R> R visit(FieldModifierVisitor<T, R> visitor, T param);
	
}
