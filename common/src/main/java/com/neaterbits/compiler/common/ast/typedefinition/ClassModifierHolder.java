package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class ClassModifierHolder extends BaseASTElement implements ClassModifier {

	private final ClassModifier delegate;

	public ClassModifierHolder(Context context, ClassModifier delegate) {
		super(context);

		Objects.requireNonNull(delegate);

		this.delegate = delegate;
	}

	@Override
	public <T, R> R visit(ClassModifierVisitor<T, R> visitor, T param) {
		return delegate.visit(visitor, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
