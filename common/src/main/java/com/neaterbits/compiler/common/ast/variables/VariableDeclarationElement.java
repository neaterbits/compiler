package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;

public abstract class VariableDeclarationElement extends BaseASTElement {
	private final TypeReference type;
	private final VarName name;
	private final int numDims;
	public VariableDeclarationElement(Context context, TypeReference type, VarName name, int numDims) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(name);

		this.type = type;
		this.name = name;
		this.numDims = numDims;
	}

	public final TypeReference getTypeReference() {
		return type;
	}

	public final VarName getName() {
		return name;
	}

	public final int getNumDims() {
		return numDims;
	}

	public final VariableDeclaration makeVariableDeclaration(VariableModifiers modifiers) {

		final VariableDeclaration variableDeclaration = new VariableDeclaration(
				modifiers,
				type,
				name,
				numDims);

		return variableDeclaration;
	}
}
