package com.neaterbits.compiler.common.ast.type;

import com.neaterbits.compiler.common.ast.NamespaceReference;

public abstract class NamedType extends BaseType {

	private final NamespaceReference namespace;
	private final TypeName name;

	protected NamedType(NamespaceReference namespace, TypeName name, boolean nullable) {
		super(nullable);

		this.namespace = namespace;
		this.name = name;
	}

	public final NamespaceReference getNamespace() {
		return namespace;
	}

	public final TypeName getName() {
		return name;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedType other = (NamedType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		return true;
	}
}
