package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.BaseType;

public final class VariableDeclaration {

	private final TypeReference type;
	private final VarName name;
	private final int numDims;

	public VariableDeclaration(TypeReference type, VarName name, int numDims) {
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		
		this.type = type;
		this.name = name;
		this.numDims = numDims;
	}

	public TypeReference getTypeReference() {
		return type;
	}
	
	public BaseType getType() {
		return getTypeReference().getType();
	}

	public VarName getName() {
		return name;
	}

	public int getNumDims() {
		return numDims;
	}
}
