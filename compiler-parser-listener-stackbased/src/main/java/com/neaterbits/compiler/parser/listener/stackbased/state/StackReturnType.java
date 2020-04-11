package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackReturnType<TYPE_REFERENCE> extends StackEntry implements TypeReferenceSetter<TYPE_REFERENCE> {

	private TYPE_REFERENCE type;

	public StackReturnType(ParseLogger parseLogger) {
		super(parseLogger);
	}
	
	public TYPE_REFERENCE getType() {
		return type;
	}

	@Override
	public void setTypeReference(TYPE_REFERENCE typeReference) {
		Objects.requireNonNull(typeReference);

		if (this.type != null) {
			throw new IllegalStateException("Type already set");
		}

		this.type = typeReference;
	}
}
