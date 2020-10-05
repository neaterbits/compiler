package com.neaterbits.compiler.resolver.types;

import java.util.Objects;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.compiler.codemap.TypeVariant;

public abstract class BaseTypeInfo implements ResolveTypeInfo {

	private final ScopedName scopedName;
	private final TypeVariant typeVariant;

	protected BaseTypeInfo(ScopedName scopedName, TypeVariant typeVariant) {

		Objects.requireNonNull(scopedName);
		Objects.requireNonNull(typeVariant);

		this.scopedName = scopedName;
		this.typeVariant = typeVariant;
	}

	@Override
	public final ScopedName getScopedName() {
		return scopedName;
	}
	@Override
	public final TypeVariant getTypeVariant() {
		return typeVariant;
	}

	@Override
	public final boolean isInterface() {
		return typeVariant == TypeVariant.INTERFACE;
	}

	@Override
	public final boolean isClass() {
		return typeVariant == TypeVariant.CLASS;
	}

	@Override
	public final boolean isEnum() {
		return typeVariant == TypeVariant.ENUM;
	}

	@Override
	public String toString() {
		return typeVariant.name().toLowerCase() + ' ' + scopedName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scopedName == null) ? 0 : scopedName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseTypeInfo other = (BaseTypeInfo) obj;
		if (scopedName == null) {
			if (other.scopedName != null)
				return false;
		} else if (!scopedName.equals(other.scopedName))
			return false;
		return true;
	}
}
