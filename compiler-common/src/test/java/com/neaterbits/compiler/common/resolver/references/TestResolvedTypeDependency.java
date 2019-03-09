package com.neaterbits.compiler.common.resolver.references;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.ReferenceType;

public final class TestResolvedTypeDependency implements ResolvedTypeDependency {

	private final CompleteName completeName;
	private final ReferenceType referenceType;
	private final TypeVariant typeVariant;

	public TestResolvedTypeDependency(CompleteName completeName, ReferenceType referenceType, TypeVariant typeVariant) {

		Objects.requireNonNull(completeName);
		Objects.requireNonNull(referenceType);
		Objects.requireNonNull(typeVariant);
		
		this.completeName = completeName;
		this.referenceType = referenceType;
		this.typeVariant = typeVariant;
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
		return null;
	}
	
	@Override
	public TypeVariant getTypeVariant() {
		return typeVariant;
	}

}
