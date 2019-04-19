package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.statement.ASTMutability;

public interface VariableModifierVisitor<T, R> {

	R onVariableMutability(ASTMutability mutability, T param);
	
}
