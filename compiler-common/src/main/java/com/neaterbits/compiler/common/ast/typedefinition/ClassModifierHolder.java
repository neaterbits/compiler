package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;

public final class ClassModifierHolder extends BaseModifierHolder<ClassModifier>
		implements ClassModifier {

	public ClassModifierHolder(Context context, ClassModifier delegate) {
		super(context, delegate);
	}

	@Override
	public <T, R> R visit(ClassModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
