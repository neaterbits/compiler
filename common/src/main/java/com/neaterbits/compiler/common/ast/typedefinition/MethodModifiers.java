package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class MethodModifiers extends BaseASTElement {

	private final ASTList<MethodModifierHolder> modifiers;

	public MethodModifiers(Context context, List<MethodModifierHolder> modifiers) {
		super(context);

		this.modifiers = makeList(modifiers);
	}

	public ASTList<MethodModifierHolder> getModifiers() {
		return modifiers;
	}
}
