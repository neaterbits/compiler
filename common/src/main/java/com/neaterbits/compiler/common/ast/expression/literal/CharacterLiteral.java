package com.neaterbits.compiler.common.ast.expression.literal;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;

public final class CharacterLiteral extends Literal {

	private final char value;

	public CharacterLiteral(Context context, char value) {
		super(context);
		
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onCharacterLiteral(this, param);
	}
}
