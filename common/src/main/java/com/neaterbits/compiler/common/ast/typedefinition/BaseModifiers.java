package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public abstract class BaseModifiers<MODIFIER, MODIFIER_HOLDER extends BaseModifierHolder<MODIFIER>>
			extends BaseASTElement {

	private final ASTList<MODIFIER_HOLDER> modifiers;

	protected BaseModifiers(Context context, List<MODIFIER_HOLDER> modifiers) {
		super(context);

		Objects.requireNonNull(modifiers);
		
		this.modifiers = makeList(modifiers);
	}
	
	public final ASTList<MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}

	@SuppressWarnings("unchecked")
	public final <T extends MODIFIER> T getModifier(Class<T> cl) {

		Objects.requireNonNull(cl);

		final MODIFIER_HOLDER modifierHolder = modifiers.find(modifier -> modifier.getDelegate().getClass().equals(cl));
		
		return modifierHolder != null ? (T)modifierHolder.getDelegate() : null;
	}
	
	
	public final boolean hasModifier(Class<? extends MODIFIER> cl) {
		return getModifier(cl) != null;
	}

	@Override
	protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
	}
}
