package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackCastExpression<EXPRESSION, VARIABLE_REFERENCE extends EXPRESSION, PRIMARY extends EXPRESSION, TYPE_REFERENCE>
	extends StackExpression<EXPRESSION, VARIABLE_REFERENCE, PRIMARY>
	implements TypeReferenceSetter<TYPE_REFERENCE> {

	private TYPE_REFERENCE typeReference;

	public StackCastExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public TYPE_REFERENCE getTypeReference() {
		return typeReference;
	}

	@Override
	public void setTypeReference(TYPE_REFERENCE type) {
		
		Objects.requireNonNull(type);
		
		if (this.typeReference != null) {
			throw new IllegalStateException();
		}
		
		this.typeReference = type;
	}
}
