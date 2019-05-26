package com.neaterbits.compiler.util.typedefinition;

public interface FieldModifier {

	<T, R> R visit(FieldModifierVisitor<T, R> visitor, T param);
	
}
