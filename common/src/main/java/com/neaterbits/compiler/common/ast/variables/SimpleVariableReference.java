package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;

public final class SimpleVariableReference extends VariableReference {
	private final VariableDeclaration declaration;

	public SimpleVariableReference(Context context, VariableDeclaration declaration) {
		super(context);

		Objects.requireNonNull(declaration);

		this.declaration = declaration;
	}

	public VariableDeclaration getDeclaration() {
		return declaration;
	}
	
	public VarName getName() {
		return declaration.getName();
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onSimpleVariableReference(this, param);
	}
}
