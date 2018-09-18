package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(expression, recurseMode, visitor);
	}
}
