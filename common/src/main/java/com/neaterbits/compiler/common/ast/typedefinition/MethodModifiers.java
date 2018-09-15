package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Collections;
import java.util.List;

public final class MethodModifiers {

	private final List<MethodModifier> modifiers;

	public MethodModifiers(List<MethodModifier> modifiers) {
		this.modifiers = Collections.unmodifiableList(modifiers);
	}

	public List<MethodModifier> getModifiers() {
		return modifiers;
	}
}
