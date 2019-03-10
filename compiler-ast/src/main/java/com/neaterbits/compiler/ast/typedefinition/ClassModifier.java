package com.neaterbits.compiler.ast.typedefinition;

public interface ClassModifier {

	<T, R> R visit(ClassModifierVisitor<T, R> visitor, T param);
	
}
