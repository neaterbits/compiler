package com.neaterbits.compiler.common.ast.typedefinition;

public interface VariableModifier {
	
	<T, R> R visit(VariableModifierVisitor<T, R> visitor, T param);

}
