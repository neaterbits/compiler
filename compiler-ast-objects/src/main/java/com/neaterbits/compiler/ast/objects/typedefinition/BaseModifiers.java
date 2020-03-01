package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BasePlaceholderASTElement;
import com.neaterbits.compiler.ast.objects.list.ASTList;

public abstract class BaseModifiers<MODIFIER, MODIFIER_HOLDER extends BaseModifierHolder<MODIFIER>>
			extends BasePlaceholderASTElement {

	private final ASTList<MODIFIER_HOLDER> modifiers;

	protected BaseModifiers(List<MODIFIER_HOLDER> modifiers) {

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
