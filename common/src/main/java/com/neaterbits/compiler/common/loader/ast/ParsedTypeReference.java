package com.neaterbits.compiler.common.loader.ast;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.resolver.ReferenceType;

final class ParsedTypeReference implements CompiledTypeDependency {

	private final ScopedName scopedName;
	private final ReferenceType type;
	private final TypeReference element;
	
	ParsedTypeReference(ScopedName scopedName, ReferenceType type, TypeReference element) {
	
		Objects.requireNonNull(scopedName);
		Objects.requireNonNull(type);
		Objects.requireNonNull(element);
		
		this.scopedName = scopedName;
		this.type = type;
		this.element = element;
	}

	@Override
	public ScopedName getScopedName() {
		return scopedName;
	}

	@Override
	public ReferenceType getReferenceType() {
		return type;
	}
	
	@Override
	public TypeReference getElement() {
		return element;
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
		return "ParsedTypeReference [scopedName=" + scopedName + "]";
	}
	
	
}
