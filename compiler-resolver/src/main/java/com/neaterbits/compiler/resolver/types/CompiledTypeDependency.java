package com.neaterbits.compiler.resolver.types;

import java.util.Objects;

import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.model.UpdateOnResolve;

public final class CompiledTypeDependency {
	
	private final ScopedName scopedName;
	private final ReferenceType type;
	private final int typeReferenceElementRef;
	private final UpdateOnResolve updateOnResolve;
	private final Integer updateOnResolveElementRef;
	
	public CompiledTypeDependency(
			ScopedName scopedName,
			ReferenceType type,
			int typeReferenceElementRef,
			UpdateOnResolve updateOnResolve,
			Integer updateOnResolveElementRef) {
		
		Objects.requireNonNull(scopedName);
		Objects.requireNonNull(type);
		
		this.scopedName = scopedName;
		this.type = type;
		this.typeReferenceElementRef = typeReferenceElementRef;
		this.updateOnResolve = updateOnResolve;
		this.updateOnResolveElementRef = updateOnResolveElementRef;
	}

	public ScopedName getScopedName() {
		return scopedName;
	}

	public final ReferenceType getReferenceType() {
		return type;
	}

	public int getTypeReferenceElementRef() {
		return typeReferenceElementRef;
	}

	int getElement() {
		return typeReferenceElementRef;
	}

	public UpdateOnResolve getUpdateOnResolve() {
		return updateOnResolve;
	}

	public Integer getUpdateOnResolveElementRef() {
		return updateOnResolveElementRef;
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
		CompiledTypeDependency other = (CompiledTypeDependency) obj;
		if (scopedName == null) {
			if (other.scopedName != null)
				return false;
		} else if (!scopedName.equals(other.scopedName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return scopedName.toString();
	}
	
}
