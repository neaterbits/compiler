package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

public final class ClassModifiers extends BaseModifiers<ClassModifier, ClassModifierHolder> {
	
	public ClassModifiers(List<ClassModifierHolder> modifiers) {
		super(modifiers);
	}
}
