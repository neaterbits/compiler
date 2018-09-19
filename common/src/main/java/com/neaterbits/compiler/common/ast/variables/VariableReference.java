package com.neaterbits.compiler.common.ast.variables;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;

public abstract class VariableReference extends Primary {

	public abstract <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param);
	
	public VariableReference(Context context) {
		super(context);
	}

	@Override
	public final <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onVariableReference(this, param);
	}
}
