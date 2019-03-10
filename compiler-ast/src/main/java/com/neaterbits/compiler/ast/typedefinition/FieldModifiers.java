package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

public final class FieldModifiers extends BaseModifiers<FieldModifier, FieldModifierHolder> {
	
	public FieldModifiers(List<FieldModifierHolder> modifiers) {
		super(modifiers);
	}
}
