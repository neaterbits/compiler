package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.types.TypeName;

class VariableScope {

	private Map<String, VariableDeclaration> scopeVariableDeclarations;
	
	VariableScope() {

	}
	
	void addVariableDeclaration(long modelRef, int crossReferenceTokenNo, String name, TypeName type) {

		Objects.requireNonNull(name);
		Objects.requireNonNull(type);
	
		final VariableDeclaration variableDeclaration = new VariableDeclaration(crossReferenceTokenNo, type);
		
		if (scopeVariableDeclarations == null) {
			scopeVariableDeclarations = new HashMap<>();
		}
		
		scopeVariableDeclarations.put(name, variableDeclaration);
	}
	
	VariableDeclaration getVariableDeclaration(String name) {
		
		Objects.requireNonNull(name);
		
		return scopeVariableDeclarations != null
		        ? scopeVariableDeclarations.get(name)
                : null;
	}
}