package com.neaterbits.compiler.util.typedefinition;

public interface VariableModifier {
	
	<T, R> R visit(VariableModifierVisitor<T, R> visitor, T param);

}
