package com.neaterbits.compiler.common.ast.type;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.ast.NamespaceReference;

public final class FullTypeName {

	private final NamespaceReference namespace; // "package" in Java
	private final List<TypeName> outerTypes; // If this is an inner class
	private final TypeName name; // Name of the type itself

	public FullTypeName(NamespaceReference namespace, List<TypeName> outerTypes, TypeName name) {
		this.namespace = namespace;
		this.outerTypes = outerTypes != null ? Collections.unmodifiableList(outerTypes) : null;
		this.name = name;
	}
	
	public NamespaceReference getNamespace() {
		return namespace;
	}

	public List<TypeName> getOuterTypes() {
		return outerTypes;
	}

	public TypeName getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((outerTypes == null) ? 0 : outerTypes.hashCode());
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
		FullTypeName other = (FullTypeName) obj;
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
		if (outerTypes == null) {
			if (other.outerTypes != null)
				return false;
		} else if (!outerTypes.equals(other.outerTypes))
			return false;
		return true;
	}
}
