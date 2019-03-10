package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.parser.StackEntry;
import com.neaterbits.compiler.ast.parser.TypeReferenceSetter;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
