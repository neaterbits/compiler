package com.neaterbits.compiler.ast.expression.literal;

import com.neaterbits.compiler.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.type.primitive.CharacterType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class CharacterLiteral extends Literal {

	private final char value;

	public CharacterLiteral(Context context, char value, CharacterType type) {
		super(context, type);
		
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
