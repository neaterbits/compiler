package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class VariableModifiers {

	private final List<VariableModifier> modifiers;

	public VariableModifiers(List<VariableModifier> modifiers) {
		
		Objects.requireNonNull(modifiers);
		
		this.modifiers = Collections.unmodifiableList(modifiers);
	}

	public List<VariableModifier> getModifiers() {
		return modifiers;
	}
}
