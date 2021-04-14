package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;

public abstract class BaseModifierHolder<MODIFIER> extends BaseASTElement {
	private final MODIFIER delegate;

	protected BaseModifierHolder(Context context, MODIFIER delegate) {
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
