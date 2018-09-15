package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;

public class SimpleVariableReference extends VariableReference {
	private final VariableDeclaration declaration;
	private final boolean declaredHere;

	public SimpleVariableReference(Context context, VariableDeclaration declaration, boolean declaredHere) {
		super(context);

		Objects.requireNonNull(declaration);

		this.declaration = declaration;
		this.declaredHere = declaredHere;
	}

	public VariableDeclaration getDeclaration() {
		return declaration;
	}
	
	public boolean isDeclaredHere() {
		return declaredHere;
	}

	public VarName getName() {
		return declaration.getName();
	}
}
