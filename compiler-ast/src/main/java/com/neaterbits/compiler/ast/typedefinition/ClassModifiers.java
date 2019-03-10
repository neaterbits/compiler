package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

public final class ClassModifiers extends BaseModifiers<ClassModifier, ClassModifierHolder> {
	
	public ClassModifiers(List<ClassModifierHolder> modifiers) {
		super(modifiers);
	}
}
