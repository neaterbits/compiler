package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.statement.Mutability;

public interface VariableModifierVisitor<T, R> {

	R onVariableMutability(Mutability mutability, T param);
	
}
