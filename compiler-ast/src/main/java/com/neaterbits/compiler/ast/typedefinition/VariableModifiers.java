package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

public final class VariableModifiers extends BaseModifiers<VariableModifier, VariableModifierHolder> {

	public VariableModifiers(List<VariableModifierHolder> modifiers) {
		super(modifiers);
	}
}
