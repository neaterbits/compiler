package com.neaterbits.compiler.util.typedefinition;

public interface InterfaceMethodModifier {

	<T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param);
	
}
