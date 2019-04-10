package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackVariableDeclaration extends StackExpressionList implements VariableNameSetter {

	private String name;
	private Context nameContext;
	private int numDims;

	public StackVariableDeclaration(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void init(String name, Context nameContext, int numDims) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);
		
		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
		this.nameContext = nameContext;
		this.numDims = numDims;
	}

	public final String getName() {
		return name;
	}

	public final Context getNameContext() {
		return nameContext;
	}

	public final int getNumDims() {
		return numDims;
	}
}
