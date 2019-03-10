package com.neaterbits.compiler.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
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
