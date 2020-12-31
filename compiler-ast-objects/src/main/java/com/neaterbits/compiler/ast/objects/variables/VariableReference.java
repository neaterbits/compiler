package com.neaterbits.compiler.ast.objects.variables;

import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.util.parse.context.Context;

public abstract class VariableReference extends ResolvedPrimary {

	public abstract <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param);
	
	public VariableReference(Context context) {
		super(context);
	}

	@Override
	public final <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onVariableReference(this, param);
	}
}
