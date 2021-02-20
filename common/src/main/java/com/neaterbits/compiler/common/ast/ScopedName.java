package com.neaterbits.compiler.common.ast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScopedName {

	private final List<String> scope;
	private final String name;

	public ScopedName(List<String> scope, String name) {
		
		Objects.requireNonNull(name);
		
		this.scope = scope != null ? Collections.unmodifiableList(scope) : null;
		this.name = name;
	}

	public List<String> getScope() {
		return scope;
	}

	public String getName() {
		return name;
	}
}
