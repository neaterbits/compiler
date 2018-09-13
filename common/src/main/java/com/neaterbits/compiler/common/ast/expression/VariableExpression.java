package com.neaterbits.compiler.common.ast.expression;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.variables.VariableReference;

public final class VariableExpression extends Expression {

	private final VariableReference reference;

	public VariableExpression(Context context, VariableReference reference) {
		super(context);
		
		this.reference = reference;
	}

	public final VariableReference getReference() {
		return reference;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onVariable(this, param);
	}
}
