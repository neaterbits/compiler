package com.neaterbits.compiler.common.resolver;

import java.util.Objects;
import java.util.function.BiConsumer;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.loader.ast.TypeResolveMode;

final class ResolvedTypeDependencyImpl implements ResolvedTypeDependency {
	private final CompleteName completeName;
	private final ReferenceType referenceType;
	private final TypeReference element;
	private final TypeResolveMode typeResolveMode;
	private final TypeVariant typeVariant;
	private final BiConsumer<BaseType, TypeResolveMode> updateOnResolve;
	
	ResolvedTypeDependencyImpl(
			CompleteName completeName,
			ReferenceType referenceType,
			TypeReference element,
			TypeResolveMode typeResolveMode,
			TypeVariant typeVariant,
			BiConsumer<BaseType, TypeResolveMode> updateOnResolve) {

		Objects.requireNonNull(completeName);
		Objects.requireNonNull(referenceType);
		Objects.requireNonNull(element);
		Objects.requireNonNull(typeResolveMode);
		Objects.requireNonNull(typeVariant);

		this.completeName = completeName;
		this.referenceType = referenceType;
		this.element = element;
		this.typeResolveMode = typeResolveMode;
		this.typeVariant = typeVariant;
		this.updateOnResolve = updateOnResolve;
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
	
	@Override
	public TypeVariant getTypeVariant() {
		return typeVariant;
	}

	TypeResolveMode getTypeResolveMode() {
		return typeResolveMode;
	}

	BiConsumer<BaseType, TypeResolveMode> getUpdateOnResolve() {
		return updateOnResolve;
	}
}
