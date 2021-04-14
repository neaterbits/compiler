package dev.nimbler.compiler.resolver.passes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VariableScope<T extends ScopeVariableDeclaration> {

	private Map<String, T> scopeVariableDeclarations;
	
	VariableScope() {

	}
	
	public final void addVariableDeclaration(long modelRef, String name, T scopeVariableDeclaration) {

		Objects.requireNonNull(name);
		Objects.requireNonNull(scopeVariableDeclaration);
	
		if (scopeVariableDeclarations == null) {
			scopeVariableDeclarations = new HashMap<>();
		}
		
		scopeVariableDeclarations.put(name, scopeVariableDeclaration);
	}
	
	final T getVariableDeclaration(String name) {
		
		Objects.requireNonNull(name);
		
		return scopeVariableDeclarations != null
		        ? scopeVariableDeclarations.get(name)
                : null;
	}
}