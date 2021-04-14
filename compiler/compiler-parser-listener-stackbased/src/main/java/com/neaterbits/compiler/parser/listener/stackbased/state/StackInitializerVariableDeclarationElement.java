package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.util.parse.context.Context;

public final class StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION> {

	private Context context;
	private String varName;
	private Context varNameContext;
	private int numDims;
	private EXPRESSION initializer;
	
	public StackInitializerVariableDeclarationElement(Context context, String varName, Context varNameContext, int numDims, EXPRESSION initializer) {
		this.context = context;
		this.varName = varName;
		this.varNameContext = varNameContext;
		this.numDims = numDims;
		this.initializer = initializer;
	}

	public Context getContext() {
		return context;
	}

	public String getVarName() {
		return varName;
	}

	public Context getVarNameContext() {
		return varNameContext;
	}

	public int getNumDims() {
		return numDims;
	}

	public EXPRESSION getInitializer() {
		return initializer;
	}
}
