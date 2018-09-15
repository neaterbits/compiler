package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

public final class StackVariableDeclaration extends StackExpression {

	private final String name;
	private final int numDims;

	public StackVariableDeclaration(String name, int numDims) {
		
		Objects.requireNonNull(name);
		
		this.name = name;
		this.numDims = numDims;
	}

	public String getName() {
		return name;
	}

	public int getNumDims() {
		return numDims;
	}
}
