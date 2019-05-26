package com.neaterbits.compiler.util.typedefinition;

public enum ClassMethodVisibility implements ClassMethodModifier {
	PUBLIC,
	PRIVATE,
	NAMESPACE,
	NAMESPACE_AND_SUBCLASSES;

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onVisibility(this, param);
	}
}
