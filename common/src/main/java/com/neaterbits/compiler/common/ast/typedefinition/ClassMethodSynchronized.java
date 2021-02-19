package com.neaterbits.compiler.common.ast.typedefinition;

public final class ClassMethodSynchronized implements ClassMethodModifier {

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onSynchronized(this, param);
	}
}
