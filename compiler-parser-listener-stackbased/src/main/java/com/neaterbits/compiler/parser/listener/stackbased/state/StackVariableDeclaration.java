package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackVariableDeclaration<EXPRESSION, NESTED_EXPRESSION extends EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY>
	extends StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
	implements VariableNameSetter {

	private String name;
	private Context nameContext;
	private int numDims;

	public StackVariableDeclaration(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void init(String name, Context nameContext, int numDims) {
		Objects.requireNonNull(name);
		
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
