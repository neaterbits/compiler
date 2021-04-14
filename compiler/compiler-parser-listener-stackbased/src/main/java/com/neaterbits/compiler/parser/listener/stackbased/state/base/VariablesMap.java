package com.neaterbits.compiler.parser.listener.stackbased.state.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class VariablesMap<VARIABLE_DECLARATION> {

	private final Map<String, VARIABLE_DECLARATION> variables;

	public VariablesMap() {
		this.variables = new HashMap<>();
	}

	public final VARIABLE_DECLARATION findVariable(String name) {
		Objects.requireNonNull(name);

		return variables.get(name);
	}
	
	public final boolean hasVariable(String name) {
		return findVariable(name) != null;
	}

	public final void add(String name, VARIABLE_DECLARATION declaration) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(declaration);

		if (variables.containsKey(name)) {
			throw new IllegalStateException("Already added " + name);
		}

		variables.put(name, declaration);
	}

	@Override
	public String toString() {
		return variables.toString();
	}
}
