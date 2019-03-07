package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;

public final class ConstructorModifierHolder extends BaseModifierHolder<ConstructorModifier> implements ConstructorModifier {

	public ConstructorModifierHolder(Context context, ConstructorModifier modifier) {
		super(context, modifier);
	}
	
	@Override
	public <T, R> R visit(ConstructorModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
