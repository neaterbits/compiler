package com.neaterbits.compiler.ast.objects.expression.literal;

import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.type.primitive.CharacterType;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class CharacterLiteral extends Literal {

	private final char value;

	public CharacterLiteral(Context context, char value, CharacterType type, int typeNo) {
		super(context, type, typeNo);

		this.value = value;
	}

	public char getValue() {
		return value;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CHARACTER_LITERAL;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onCharacterLiteral(this, param);
	}
}
