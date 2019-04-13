package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.parser.TypeReferenceSetter;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackCastExpression extends StackExpression implements TypeReferenceSetter {

	private TypeReference typeReference;

	public StackCastExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public TypeReference getTypeReference() {
		return typeReference;
	}

	@Override
	public void setTypeReference(TypeReference type) {
		
		Objects.requireNonNull(type);
		
		if (this.typeReference != null) {
			throw new IllegalStateException();
		}
		
		this.typeReference = type;
	}
}
