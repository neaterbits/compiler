package com.neaterbits.compiler.util;

import java.util.Arrays;
import java.util.Objects;

public final class Namespace {

	private final String [] parts;

	public Namespace(String[] parts) {
		
		Objects.requireNonNull(parts);
		
		if (parts.length == 0) {
			throw new IllegalArgumentException();
		}
		
		this.parts = parts;
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
		Namespace other = (Namespace) obj;
		if (!Arrays.equals(parts, other.parts))
			return false;
		return true;
	}
}
