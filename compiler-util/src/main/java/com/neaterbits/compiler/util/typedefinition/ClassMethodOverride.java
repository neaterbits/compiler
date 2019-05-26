package com.neaterbits.compiler.util.typedefinition;

public enum ClassMethodOverride implements ClassMethodModifier {
	ABSTRACT,
	FINAL;

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onOverride(this, param);
	}
}
