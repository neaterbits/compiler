package com.neaterbits.compiler.resolver.ast;

import java.util.Objects;
import java.util.function.BiConsumer;

import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeResolveMode;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.typereference.TypeReference;

final class ParsedTypeReference implements CompiledTypeDependency {

	private final ScopedName scopedName;
	private final ReferenceType type;
	private final TypeReference element;
	private final BiConsumer<BaseType, TypeResolveMode> updateOnResolve;
	
	ParsedTypeReference(ScopedName scopedName, ReferenceType type, TypeReference element, BiConsumer<BaseType, TypeResolveMode> updateOnResolve) {
	
		Objects.requireNonNull(scopedName);
		Objects.requireNonNull(type);
		Objects.requireNonNull(element);
		
		this.scopedName = scopedName;
		this.type = type;
		this.element = element;
		this.updateOnResolve = updateOnResolve;
	}

	@Override
	public ScopedName getScopedName() {
		return scopedName;
	}

	@Override
	public ReferenceType getReferenceType() {
		return type;
	}

	TypeReference getElement() {
		return element;
	}

	BiConsumer<BaseType, TypeResolveMode> getUpdateOnResolve() {
		return updateOnResolve;
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
		ParsedTypeReference other = (ParsedTypeReference) obj;
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
