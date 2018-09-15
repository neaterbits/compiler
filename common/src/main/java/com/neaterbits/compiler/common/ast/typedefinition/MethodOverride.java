package com.neaterbits.compiler.common.ast.typedefinition;

public enum MethodOverride implements MethodModifier {
	ABSTRACT,
	FINAL;

	@Override
	public <T, R> R visit(MethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onOverride(this, param);
	}
}
