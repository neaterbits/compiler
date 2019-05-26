package com.neaterbits.compiler.util.parse.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.StackEntry;
import com.neaterbits.compiler.util.parse.stackstate.setters.TypeReferenceSetter;

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
