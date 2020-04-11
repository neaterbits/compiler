package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.Context;

public final class StackInitializerVariableDeclarationElement<TYPE_REFERENCE, EXPRESSION> {

	private Context context;
	private TYPE_REFERENCE typeReference;
	private String varName;
	private Context varNameContext;
	private int numDims;
	private EXPRESSION initializer;
	
	public StackInitializerVariableDeclarationElement(Context context, TYPE_REFERENCE typeReference, String varName, Context varNameContext, int numDims, EXPRESSION initializer) {
		this.context = context;
		this.typeReference = typeReference;
		this.varName = varName;
		this.varNameContext = varNameContext;
		this.numDims = numDims;
		this.initializer = initializer;
	}

	public Context getContext() {
		return context;
	}

	public TYPE_REFERENCE getTypeReference() {
		return typeReference;
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
