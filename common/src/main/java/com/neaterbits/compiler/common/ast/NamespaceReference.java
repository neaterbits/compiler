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
}
