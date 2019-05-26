package com.neaterbits.compiler.util.typedefinition;

import statement.ASTMutability;

public interface VariableModifierVisitor<T, R> {

	R onVariableMutability(ASTMutability mutability, T param);
	
}
