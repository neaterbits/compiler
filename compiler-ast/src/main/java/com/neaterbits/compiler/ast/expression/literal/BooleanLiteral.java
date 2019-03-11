package com.neaterbits.compiler.ast.expression.literal;

import com.neaterbits.compiler.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.util.Context;

public final class BooleanLiteral extends Literal {

	private final boolean value;
	
	public BooleanLiteral(Context context, boolean value, BooleanType booleanType) {
		super(context, booleanType);

		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onBooleanLiteral(this, param);
	}
}