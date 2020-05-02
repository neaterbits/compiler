package com.neaterbits.compiler.util.typedefinition;

import com.neaterbits.compiler.util.statement.ASTMutability;

public interface VariableModifierVisitor<T, R> {

	R onVariableMutability(ASTMutability mutability, T param);
	
}
