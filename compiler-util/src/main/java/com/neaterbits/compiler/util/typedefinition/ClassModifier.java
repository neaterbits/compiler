package com.neaterbits.compiler.util.typedefinition;

public interface ClassModifier {

	<T, R> R visit(ClassModifierVisitor<T, R> visitor, T param);
	
}
