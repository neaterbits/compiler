package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class MethodModifierHolder extends BaseASTElement implements MethodModifier {

	private final MethodModifier delegate;

	public MethodModifierHolder(Context context, MethodModifier delegate) {
		super(context);

		Objects.requireNonNull(delegate);

		this.delegate = delegate;
	}

	@Override
	public <T, R> R visit(MethodModifierVisitor<T, R> visitor, T param) {
		return delegate.visit(visitor, param);
	}
}
