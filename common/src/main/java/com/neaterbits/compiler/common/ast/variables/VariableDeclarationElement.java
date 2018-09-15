package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.expression.Expression;

public final class VariableDeclarationElement extends BaseASTElement {

	private final VariableDeclaration declaration;
	private final Expression initializer;
	
	public VariableDeclarationElement(Context context, VariableDeclaration declaration, Expression initializer) {
		super(context);

		Objects.requireNonNull(declaration);

		this.declaration = declaration;
		this.initializer = initializer;
	}

	public VariableDeclaration getDeclaration() {
		return declaration;
	}

	public Expression getInitializer() {
		return initializer;
	}
}
