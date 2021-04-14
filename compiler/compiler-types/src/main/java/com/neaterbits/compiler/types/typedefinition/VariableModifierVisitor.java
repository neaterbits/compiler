package com.neaterbits.compiler.types.typedefinition;

import com.neaterbits.compiler.types.statement.ASTMutability;

public interface VariableModifierVisitor<T, R> {

	R onVariableMutability(ASTMutability mutability, T param);
	
}
