package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public class ClassModifiers extends BaseASTElement {
	
	private final ASTList<ClassModifierHolder> modifiers;

	public ClassModifiers(Context context, List<ClassModifierHolder> modifiers) {
		super(context);

		Objects.requireNonNull(modifiers);

		this.modifiers = makeList(modifiers);
	}

	public ASTList<? extends ClassModifier> getModifiers() {
		return modifiers;
	}
}
