package com.neaterbits.compiler.ast;

import java.util.Arrays;
import java.util.Collection;

import com.neaterbits.compiler.util.Strings;

public final class NamespaceReference {

	private final String [] parts;

	public NamespaceReference(String [] parts) {
		this.parts = Arrays.copyOf(parts, parts.length);
	}
	
	public NamespaceReference(Collection<String> parts) {
		this.parts = parts.toArray(new String[parts.size()]);
	}

	public String [] getParts() {
		return Arrays.copyOf(parts, parts.length);
	}
	
	public boolean startsWith(String [] parts) {
		return Strings.startsWith(this.parts, parts);
	}

	public NamespaceReference removeFromNamespace(String [] parts) {
		
		if (!startsWith(parts)) {
			throw new IllegalArgumentException("Does not start with parts " + Arrays.toString(parts));
		}

		return new NamespaceReference(Strings.lastOf(this.parts, this.parts.length - parts.length));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(parts);
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
		if (!Arrays.equals(parts, other.parts))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Arrays.toString(parts);
	}
}
