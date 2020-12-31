package com.neaterbits.compiler.resolver.passes;

import com.neaterbits.build.types.TypeName;

public class ScopeVariableDeclaration {
	private final TypeName type;

	public ScopeVariableDeclaration(TypeName type) {
		this.type = type;
	}

	public TypeName getType() {
		return type;
	}
}