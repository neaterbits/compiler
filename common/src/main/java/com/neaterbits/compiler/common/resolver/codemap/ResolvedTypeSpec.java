package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.loader.TypeVariant;

final class ResolvedTypeSpec {

	private final FullTypeName name;
	private final TypeVariant typeVariant;

	ResolvedTypeSpec(FullTypeName name, TypeVariant typeVariant) {
		
		Objects.requireNonNull(name);
		Objects.requireNonNull(typeVariant);
		
		this.name = name;
		this.typeVariant = typeVariant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((typeVariant == null) ? 0 : typeVariant.hashCode());
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
		ResolvedTypeSpec other = (ResolvedTypeSpec) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (typeVariant != other.typeVariant)
			return false;
		return true;
	}
}
