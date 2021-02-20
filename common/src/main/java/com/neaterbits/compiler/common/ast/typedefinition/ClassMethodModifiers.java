package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class ClassMethodModifiers extends BaseASTElement {

	private final ASTList<ClassMethodModifierHolder> modifiers;

	public ClassMethodModifiers(Context context, List<ClassMethodModifierHolder> modifiers) {
		super(context);

		this.modifiers = makeList(modifiers);
	}

	public ASTList<ClassMethodModifierHolder> getModifiers() {
		return modifiers;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
	}
}
