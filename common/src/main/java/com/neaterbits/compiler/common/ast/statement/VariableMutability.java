package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifierVisitor;

public enum VariableMutability implements VariableModifier {

	VALUE_OR_REF_IMMUTABLE,				// final in java
	VALUE_OR_OBJECT_IMMUTABLE,			// const in C++ ?
	VALUE_OR_OBJECT_OR_REF_IMMUTABLE;	// const & const in C++ ?
	
	@Override
	public <T, R> R visit(VariableModifierVisitor<T, R> visitor, T param) {
		return visitor.onVariableMutability(this, param);
	}
}
