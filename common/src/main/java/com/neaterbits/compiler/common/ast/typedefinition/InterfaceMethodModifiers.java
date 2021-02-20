package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class InterfaceMethodModifiers extends BaseASTElement {

	private final ASTList<InterfaceMethodModifierHolder> modifiers;

	public InterfaceMethodModifiers(Context context, List<InterfaceMethodModifierHolder> modifiers) {
		super(context);
		
		Objects.requireNonNull(modifiers);
		
		this.modifiers = makeList(modifiers);
	}

	public ASTList<InterfaceMethodModifierHolder> getModifiers() {
		return modifiers;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
	}
}
