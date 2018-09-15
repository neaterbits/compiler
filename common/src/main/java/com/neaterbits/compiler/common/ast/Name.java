package com.neaterbits.compiler.common.ast;

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
}
