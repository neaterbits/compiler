package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackVariableDeclaration extends StackExpressionList implements VariableNameSetter {

	private String name;
	private int numDims;

	public StackVariableDeclaration(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void init(String name, int numDims) {
		Objects.requireNonNull(name);

		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
		this.numDims = numDims;
	}

	public final String getName() {
		return name;
	}

	public final int getNumDims() {
		return numDims;
	}
}
