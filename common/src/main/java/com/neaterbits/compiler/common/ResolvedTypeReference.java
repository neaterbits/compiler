package com.neaterbits.compiler.common;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.BaseType;

public final class ResolvedTypeReference extends TypeReference {

	private final BaseType type;

	public ResolvedTypeReference(BaseType type) {

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	@Override
	public BaseType getType() {
		return type;
	}
}
