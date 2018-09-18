package com.neaterbits.compiler.common.ast.typedefinition;

public interface FieldModifier {

	<T, R> R visit(FieldModifierVisitor<T, R> visitor, T param);
	
}
