package com.neaterbits.compiler.common.ast.typedefinition;

public interface InterfaceModifier {

	<T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param);
	
}
