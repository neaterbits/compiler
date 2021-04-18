package dev.nimbler.compiler.ast.objects.variables;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;

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
