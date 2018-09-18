package com.neaterbits.compiler.common.ast.variables;

import com.neaterbits.compiler.common.Context;

public final class ArrayAccessReference extends VariableReference {
	public ArrayAccessReference(Context context) {
		super(context);
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onArrayAccessReference(this, param);
	}
}
