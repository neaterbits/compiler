package com.neaterbits.compiler.types.typedefinition;

public interface InterfaceModifier {

	<T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param);
	
}
