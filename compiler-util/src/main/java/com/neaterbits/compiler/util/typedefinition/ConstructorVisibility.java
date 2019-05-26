package com.neaterbits.compiler.util.typedefinition;

public enum ConstructorVisibility implements ConstructorModifier {
	
	PUBLIC,
	PRIVATE,
	NAMESPACE,
	NAMESPACE_AND_SUBCLASSES;

	@Override
	public <T, R> R visit(ConstructorModifierVisitor<T, R> visitor, T param) {
		return visitor.onVisibility(this, param);
	}
}
