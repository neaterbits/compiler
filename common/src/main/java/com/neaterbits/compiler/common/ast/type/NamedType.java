package com.neaterbits.compiler.common.ast.type;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.NamespaceReference;

public abstract class NamedType extends BaseType {

	private final FullTypeName fullTypeName;
	
	protected NamedType(FullTypeName fullTypeName, boolean nullable) {
		super(nullable);

		Objects.requireNonNull(fullTypeName);
		
		this.fullTypeName = fullTypeName;
	}

	public final NamespaceReference getNamespace() {
		return fullTypeName.getNamespace();
	}

	public final TypeName getName() {
		return fullTypeName.getName();
	}
	
	public final FullTypeName getFullTypeName() {
		return fullTypeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fullTypeName == null) ? 0 : fullTypeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedType other = (NamedType) obj;
		if (fullTypeName == null) {
			if (other.fullTypeName != null)
				return false;
		} else if (!fullTypeName.equals(other.fullTypeName))
			return false;
		return true;
	}
}
