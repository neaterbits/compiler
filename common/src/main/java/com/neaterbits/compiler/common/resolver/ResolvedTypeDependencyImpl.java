package com.neaterbits.compiler.common.resolver;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;

final class ResolvedTypeDependencyImpl implements ResolvedTypeDependency {
	private final FullTypeName fullTypeName;
	private final ReferenceType referenceType;
	private final TypeReference element;
	
	ResolvedTypeDependencyImpl(FullTypeName fullTypeName, ReferenceType referenceType, TypeReference element) {

		Objects.requireNonNull(fullTypeName);
		Objects.requireNonNull(referenceType);
		Objects.requireNonNull(element);

		this.fullTypeName = fullTypeName;
		this.referenceType = referenceType;
		this.element = element;
	}

	@Override
	public FullTypeName getFullTypeName() {
		return fullTypeName;
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
