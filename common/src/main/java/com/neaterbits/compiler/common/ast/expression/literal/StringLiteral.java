package com.neaterbits.compiler.common.ast.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;

public final class StringLiteral extends Literal {

	private final String value;
	
	public StringLiteral(Context context, String value, StringType type) {
		super(context, type);

		Objects.requireNonNull(value);
		
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onStringLiteral(this, param);
	}
}
