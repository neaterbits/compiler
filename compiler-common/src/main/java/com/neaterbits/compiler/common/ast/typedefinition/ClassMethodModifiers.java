package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;

public final class ClassMethodModifiers extends BaseModifiers<ClassMethodModifier, ClassMethodModifierHolder> {

	public ClassMethodModifiers(Context context, List<ClassMethodModifierHolder> modifiers) {
		super(context, modifiers);
	}
}
