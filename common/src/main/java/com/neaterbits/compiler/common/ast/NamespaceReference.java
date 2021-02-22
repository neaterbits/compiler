package com.neaterbits.compiler.common.ast;

import java.util.Arrays;

public final class NamespaceReference {

	private final String name;
	private final String [] parts;

	public NamespaceReference(String name, String [] parts) {
		this.name = name;
		this.parts = Arrays.copyOf(parts, parts.length);
	}

	public String getName() {
		return name;
	}

	public String [] getParts() {
		return Arrays.copyOf(parts, parts.length);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		NamespaceReference other = (NamespaceReference) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
