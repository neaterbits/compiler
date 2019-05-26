package com.neaterbits.compiler.util.typedefinition;

public enum InterfaceVisibility implements InterfaceModifier {

	PUBLIC,
	NAMESPACE,
	PRIVATE;

	@Override
	public <T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param) {
		return visitor.onVisibility(this, param);
	}
}
