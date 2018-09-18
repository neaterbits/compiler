package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.ast.typedefinition.FieldModifier;
import com.neaterbits.compiler.common.ast.typedefinition.FieldModifierVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifierVisitor;

public enum Mutability implements VariableModifier, FieldModifier {

	VALUE_OR_REF_IMMUTABLE,				// final in java
	VALUE_OR_OBJECT_IMMUTABLE,			// const in C++ ?
	VALUE_OR_OBJECT_OR_REF_IMMUTABLE;	// const & const in C++ ?
	
	@Override
	public <T, R> R visit(VariableModifierVisitor<T, R> visitor, T param) {
		return visitor.onVariableMutability(this, param);
	}

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onFieldMutability(this, param);
	}
}
