package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class UnaryExpression extends Expression {

	private final ASTSingle<Expression> expression;

	public UnaryExpression(Context context, Expression expression) {
		super(context);

		Objects.requireNonNull(expression);

		this.expression = makeSingle(expression);
	}

	public final Expression getExpression() {
		return expression.get();
	}
	
	@Override
	public final TypeReference getType() {
		return expression.get().getType();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expression, recurseMode, iterator);
	}
}