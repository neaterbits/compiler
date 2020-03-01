package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.util.Context;

public abstract class BaseModifierHolder<MODIFIER> extends BaseASTElement {
	private final MODIFIER delegate;

	BaseModifierHolder(Context context, MODIFIER delegate) {
		super(context);

		Objects.requireNonNull(delegate);

		this.delegate = delegate;
	}

	final MODIFIER getDelegate() {
		return delegate;
	}

	public final MODIFIER getModifier() {
		return delegate;
	}
	
	@Override
	protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
