package com.neaterbits.compiler.common.ast.typedefinition;

public interface ClassModifier {

	<T, R> R visit(ClassModifierVisitor<T, R> visitor, T param);
	
}
