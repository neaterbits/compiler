package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;

public final class VariableDeclarationElement extends BaseASTElement {

	private final TypeReference type;
	private final VarName name;
	private final int numDims;
	private final Expression initializer;
	
	public VariableDeclarationElement(Context context, TypeReference type, VarName name, int numDims, Expression initializer) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(name);

		this.type = type;
		this.name = name;
		this.numDims = numDims;

		this.initializer = initializer;
	}

	public TypeReference getTypeReference() {
		return type;
	}

	public VarName getName() {
		return name;
	}

	public int getNumDims() {
		return numDims;
	}

	public Expression getInitializer() {
		return initializer;
	}
	
	public VariableDeclaration makeVariableDeclaration(VariableModifiers modifiers) {

		final VariableDeclaration variableDeclaration = new VariableDeclaration(
				modifiers,
				type,
				name,
				numDims);

		return variableDeclaration;
	}
}
