package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import com.neaterbits.build.types.TypeName;

class VariableDeclaration {
	private final int tokenNo;
	private final TypeName type;

	public VariableDeclaration(int tokenNo, TypeName type) {
		this.tokenNo = tokenNo;
		this.type = type;
	}

	public int getTokenNo() {
		return tokenNo;
	}

	public TypeName getType() {
		return type;
	}
}