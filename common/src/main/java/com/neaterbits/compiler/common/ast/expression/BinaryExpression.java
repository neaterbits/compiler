package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;

public abstract class BinaryExpression extends Expression {

	private final Expression lhs;
	private final Expression rhs;
	
	public BinaryExpression(Context context, Expression lhs, Expression rhs) {
		super(context);
		
		Objects.requireNonNull(lhs);
		Objects.requireNonNull(rhs);
		
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public final Expression getLhs() {
		return lhs;
	}

	public final Expression getRhs() {
		return rhs;
	}
}
