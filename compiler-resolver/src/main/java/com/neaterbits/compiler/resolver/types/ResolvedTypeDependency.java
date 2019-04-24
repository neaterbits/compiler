package com.neaterbits.compiler.resolver.types;

import java.util.Objects;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.UpdateOnResolve;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

public final class ResolvedTypeDependency {

	private final TypeName completeName;
	private final ReferenceType referenceType;
	private final int typeReferenceElement;
	private final TypeResolveMode typeResolveMode;
	private final TypeVariant typeVariant;
	private final UpdateOnResolve updateOnResolve;
	private final Integer updateOnResolveElementRef;
	
	public ResolvedTypeDependency(
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

	public ScopedName getScopedName() {
		return getCompleteName().toScopedName();
	}

	public TypeName getCompleteName() {
		return completeName;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public TypeVariant getTypeVariant() {
		return typeVariant;
	}

	public TypeResolveMode getTypeResolveMode() {
		return typeResolveMode;
	}

	public int getTypeReferenceElement() {
		return typeReferenceElement;
	}

	public UpdateOnResolve getUpdateOnResolve() {
		return updateOnResolve;
	}

	public boolean shouldUpdateOnResolve() {
		return updateOnResolve != null;
	}

	public Integer getUpdateOnResolveElementRef() {
		return updateOnResolveElementRef;
	}
}
