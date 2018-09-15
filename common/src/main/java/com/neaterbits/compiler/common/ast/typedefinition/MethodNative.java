package com.neaterbits.compiler.common.ast.typedefinition;

public final class MethodNative implements MethodModifier {

	@Override
	public <T, R> R visit(MethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onNative(this, param);
	}
}
