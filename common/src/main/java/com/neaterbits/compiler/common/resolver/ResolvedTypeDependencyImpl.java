package com.neaterbits.compiler.common.resolver;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;

final class ResolvedTypeDependencyImpl implements ResolvedTypeDependency {
	private final CompleteName completeName;
	private final ReferenceType referenceType;
	private final TypeReference element;
	
	ResolvedTypeDependencyImpl(CompleteName completeName, ReferenceType referenceType, TypeReference element) {

		Objects.requireNonNull(completeName);
		Objects.requireNonNull(referenceType);
		Objects.requireNonNull(element);

		this.completeName = completeName;
		this.referenceType = referenceType;
		this.element = element;
	}

	@Override
	public CompleteName getCompleteName() {
		return completeName;
	}

	@Override
	public ReferenceType getReferenceType() {
		return referenceType;
	}

	@Override
	public TypeReference getElement() {
		return element;
	}
}
