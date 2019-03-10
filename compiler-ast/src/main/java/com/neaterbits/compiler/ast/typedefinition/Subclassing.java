package com.neaterbits.compiler.ast.typedefinition;

public enum Subclassing implements ClassModifier {
	ABSTRACT,
	FINAL;

	@Override
	public <T, R> R visit(ClassModifierVisitor<T, R> visitor, T param) {
		return visitor.onSubclassing(this, param);
	}
}
