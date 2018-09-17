package com.neaterbits.compiler.common.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;

public final class VariablesMap {

	private final Map<String, VariableDeclaration> variables;

	public VariablesMap() {
		this.variables = new HashMap<>();
	}

	public final VariableDeclaration findVariable(String name) {
		Objects.requireNonNull(name);

		return variables.get(name);
	}

	public final void add(String name, VariableDeclaration declaration) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(declaration);
		
		variables.put(name, declaration);
	}
}
