package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;

public final class InterfaceMethodModifierHolder extends BaseModifierHolder<InterfaceMethodModifier> implements InterfaceMethodModifier {

	public InterfaceMethodModifierHolder(Context context, InterfaceMethodModifier delegate) {
		super(context, delegate);
	}

	@Override
	public <T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
