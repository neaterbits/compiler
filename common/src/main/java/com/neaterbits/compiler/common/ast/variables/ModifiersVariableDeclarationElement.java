package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifiers;

public final class ModifiersVariableDeclarationElement extends VariableDeclarationElement {

	private final VariableModifiers modifiers;

	public ModifiersVariableDeclarationElement(Context context, VariableModifiers modifiers, TypeReference type, VarName name, int numDims) {
		super(context, type, name, numDims);

		Objects.requireNonNull(modifiers);

		this.modifiers = modifiers;
	}

	public VariableModifiers getModifiers() {
		return modifiers;
	}
}
