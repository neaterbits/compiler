package com.neaterbits.compiler.common.ast.typedefinition;


import com.neaterbits.compiler.common.Context;

public final class InterfaceModifierHolder extends BaseModifierHolder<InterfaceModifier> implements InterfaceModifier {

	public InterfaceModifierHolder(Context context, InterfaceModifier modifier) {
		super(context, modifier);
	}

	@Override
	public <T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
