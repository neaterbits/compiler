package com.neaterbits.compiler.util.typedefinition;

public interface InterfaceModifier {

	<T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param);
	
}
