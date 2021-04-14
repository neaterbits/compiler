package dev.nimbler.compiler.types.typedefinition;

import dev.nimbler.compiler.types.statement.ASTMutability;

public interface VariableModifierVisitor<T, R> {

	R onVariableMutability(ASTMutability mutability, T param);
	
}
