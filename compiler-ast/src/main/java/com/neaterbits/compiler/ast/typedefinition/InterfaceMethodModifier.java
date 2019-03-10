package com.neaterbits.compiler.ast.typedefinition;

public interface InterfaceMethodModifier {

	<T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param);
	
}
