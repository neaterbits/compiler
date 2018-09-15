package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.ast.statement.VariableMutability;

public interface VariableModifierVisitor<T, R> {

	R onVariableMutability(VariableMutability mutability, T param);
	
}
