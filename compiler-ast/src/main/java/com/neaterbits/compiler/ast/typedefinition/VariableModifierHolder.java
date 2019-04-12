package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.util.Context;

public final class VariableModifierHolder extends BaseModifierHolder<VariableModifier> implements VariableModifier {

	public VariableModifierHolder(Context context, VariableModifier delegate) {
		super(context, delegate);
	}

	@Override
	public <T, R> R visit(VariableModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
