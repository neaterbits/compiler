package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.StackEntry;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;

public final class StackReturnType extends StackEntry implements TypeReferenceSetter {

	private TypeReference type;

	public StackReturnType(ParseLogger parseLogger) {
		super(parseLogger);
	}
	
	public TypeReference getType() {
		return type;
	}

	@Override
	public void setTypeReference(TypeReference typeReference) {
		Objects.requireNonNull(typeReference);

		if (this.type != null) {
			throw new IllegalStateException("Type already set");
		}

		this.type = typeReference;
	}
}
