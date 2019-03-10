package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.util.Context;

public final class VariableModifiers extends BaseASTElement {

	private final ASTList<VariableModifierHolder> modifiers;

	public VariableModifiers(Context context, List<VariableModifierHolder> modifiers) {
		super(context);

		Objects.requireNonNull(modifiers);
		
		this.modifiers = makeList(modifiers);
	}

	public ASTList<? extends VariableModifier> getModifiers() {
		return modifiers;
	}

	public ASTList<VariableModifierHolder> getModifierHolders() {
		return modifiers;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
	}
}
