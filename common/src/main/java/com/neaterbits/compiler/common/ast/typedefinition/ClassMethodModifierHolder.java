package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;

public final class ClassMethodModifierHolder extends BaseModifierHolder<ClassMethodModifier>
			implements ClassMethodModifier {

	public ClassMethodModifierHolder(Context context, ClassMethodModifier delegate) {
		super(context, delegate);
	}


	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
