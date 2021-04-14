package dev.nimbler.compiler.ast.objects.expression.literal;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;
import dev.nimbler.compiler.ast.objects.type.primitive.CharacterType;
import dev.nimbler.compiler.types.ParseTreeElement;

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
