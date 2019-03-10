package com.neaterbits.compiler.util;

import java.util.Arrays;
import java.util.Objects;

public class TypeName {

	private final String [] namespace;
	private final String [] outerTypes;
	private final String name;
	
	public TypeName(String[] namespace, String[] outerTypes, String name) {

		Objects.requireNonNull(name);
		
		this.namespace = namespace;
		this.outerTypes = outerTypes;
		this.name = name;
	}

	public final String[] getNamespace() {
		return namespace;
	}

	public final String[] getOuterTypes() {
		return outerTypes;
	}

	public final String getName() {
		return name;
	}

	public String join(char separator) {
		final StringBuilder sb = new StringBuilder();
		
		if (namespace != null) {
			sb.append(Strings.join(namespace, separator));
		}

		if (outerTypes != null) {
			sb.append(Strings.join(outerTypes, separator));
		}
		
		sb.append(separator).append(name);
		
		return sb.toString();
	}
	
	public String toDebugString() {
		return join('.');
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(namespace);
		result = prime * result + Arrays.hashCode(outerTypes);
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
		TypeName other = (TypeName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(namespace, other.namespace))
			return false;
		if (!Arrays.equals(outerTypes, other.outerTypes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TypeName [namespace=" + Arrays.toString(namespace) + ", outerTypes=" + Arrays.toString(outerTypes)
				+ ", name=" + name + "]";
	}
}
