package com.neaterbits.compiler.common.ast.typedefinition;

public final class MethodStrictfp implements MethodModifier {

	@Override
	public <T, R> R visit(MethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onStrictFp(this, param);
	}
}
