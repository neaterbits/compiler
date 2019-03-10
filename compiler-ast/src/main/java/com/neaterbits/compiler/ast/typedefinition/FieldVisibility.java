package com.neaterbits.compiler.ast.typedefinition;

public enum FieldVisibility implements FieldModifier {
	PUBLIC,
	PRIVATE,
	NAMESPACE,
	NAMESPACE_AND_SUBCLASSES;

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onFieldVisibility(this, param);
	}
}
