package com.neaterbits.compiler.common.ast.typedefinition;

public final class MethodSynchronized implements MethodModifier {

	@Override
	public <T, R> R visit(MethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onSynchronized(this, param);
	}
}
