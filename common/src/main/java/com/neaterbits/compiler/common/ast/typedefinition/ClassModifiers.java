package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassModifiers {
	
	private final List<ClassModifier> modifiers;

	public ClassModifiers(List<ClassModifier> modifiers) {
		
		Objects.requireNonNull(modifiers);

		this.modifiers = Collections.unmodifiableList(modifiers);
	}

	public List<ClassModifier> getModifiers() {
		return modifiers;
	}
}
