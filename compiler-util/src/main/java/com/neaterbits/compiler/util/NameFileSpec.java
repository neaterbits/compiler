package com.neaterbits.compiler.util;

import java.util.Objects;

public final class NameFileSpec implements FileSpec {

	private final String name;

	public NameFileSpec(String name) {

		Objects.requireNonNull(name);
		
		this.name = name;
	}
	
	@Override
	public String getDistinctName() {
		return name;
	}

	@Override
	public String getParseContextName() {
		return name;
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
		NameFileSpec other = (NameFileSpec) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NameFileSpec [name=" + name + "]";
	}
}
