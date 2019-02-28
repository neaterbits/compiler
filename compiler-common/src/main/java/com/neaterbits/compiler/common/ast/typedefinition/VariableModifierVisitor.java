package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.ast.statement.Mutability;

public interface VariableModifierVisitor<T, R> {

	R onVariableMutability(Mutability mutability, T param);
	
}
