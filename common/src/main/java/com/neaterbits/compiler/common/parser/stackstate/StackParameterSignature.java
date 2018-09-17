package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.StackEntry;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;

public final class StackParameterSignature extends StackEntry implements TypeReferenceSetter, VariableNameSetter {

	private TypeReference type;
	private String name;

	public StackParameterSignature(ParseLogger parseLogger) {
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

	@Override
	public void init(String name, int numDims) {
		
		Objects.requireNonNull(name);
		
		if (this.name != null) {
			throw new IllegalStateException("Parameter name already set");
		}
		
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
