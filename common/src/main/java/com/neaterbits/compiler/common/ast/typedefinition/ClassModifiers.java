package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;

public final class ClassModifiers extends BaseModifiers<ClassModifier, ClassModifierHolder> {
	
	public ClassModifiers(Context context, List<ClassModifierHolder> modifiers) {
		super(context, modifiers);
	}
}
