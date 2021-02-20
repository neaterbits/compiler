package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class FieldModifiers extends BaseASTElement {
	
	private final ASTList<FieldModifierHolder> modifiers;

	public FieldModifiers(Context context, List<FieldModifierHolder> modifiers) {
		super(context);
		
		this.modifiers = makeList(modifiers);
	}

	public ASTList<FieldModifierHolder> getModifiers() {
		return modifiers;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
	}
}
