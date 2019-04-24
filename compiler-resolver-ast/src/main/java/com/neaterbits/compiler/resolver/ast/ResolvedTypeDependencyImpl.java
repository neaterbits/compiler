package com.neaterbits.compiler.resolver.ast;

import java.util.Objects;

import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.UpdateOnResolve;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

final class ResolvedTypeDependencyImpl implements ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName> {
	private final TypeName completeName;
	private final ReferenceType referenceType;
	private final int typeReferenceElement;
	private final TypeResolveMode typeResolveMode;
	private final TypeVariant typeVariant;
	private final UpdateOnResolve updateOnResolve;
	private final Integer updateOnResolveElementRef;
	
	ResolvedTypeDependencyImpl(
			TypeName completeName,
			ReferenceType referenceType,
			int typeReferenceElement,
			TypeResolveMode typeResolveMode,
			TypeVariant typeVariant,
			UpdateOnResolve updateOnResolve,
			Integer updateOnResolveElementRef) {

		Objects.requireNonNull(completeName);
		Objects.requireNonNull(referenceType);
		Objects.requireNonNull(typeResolveMode);
//		Objects.requireNonNull(typeVariant);

		this.completeName = completeName;
		this.referenceType = referenceType;
		this.typeReferenceElement = typeReferenceElement;
		this.typeResolveMode = typeResolveMode;
		this.typeVariant = typeVariant;
		this.updateOnResolve = updateOnResolve;
		this.updateOnResolveElementRef = updateOnResolveElementRef;
	}

	@Override
	public TypeName getCompleteName() {
		return completeName;
	}

	@Override
	public ReferenceType getReferenceType() {
		return referenceType;
	}

	@Override
	public TypeVariant getTypeVariant() {
		return typeVariant;
	}

	@Override
	public TypeResolveMode getTypeResolveMode() {
		return typeResolveMode;
	}

	@Override
	public int getTypeReferenceElement() {
		return typeReferenceElement;
	}

	@Override
	public UpdateOnResolve getUpdateOnResolve() {
		return updateOnResolve;
	}

	@Override
	public boolean shouldUpdateOnResolve() {
		return updateOnResolve != null;
	}

	@Override
	public Integer getUpdateOnResolveElementRef() {
		return updateOnResolveElementRef;
	}
}
