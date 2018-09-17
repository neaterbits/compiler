package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.log.ParseLogger;

public final class StackVariableDeclaration extends StackExpressionList {

	private final String name;
	private final int numDims;

	public StackVariableDeclaration(ParseLogger parseLogger, String name, int numDims) {
		super(parseLogger);
		
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
