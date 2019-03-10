package com.neaterbits.compiler.ast;

import java.util.Objects;

public abstract class Name {
	private final String name;

	public static void check(String name) {
		if (!name.trim().equals(name)) {
			throw new IllegalArgumentException("name not trimmed");
		}
		
		if (name.isEmpty()) {
			throw new IllegalArgumentException("name is empty");
		}
	}
	
	public Name(String name) {
		Objects.requireNonNull(name);

		check(name);
		
		this.name = name;
	}

	public final String getName() {
		return name;
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name other = (Name) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [name=" + name + "]";
	}
}
