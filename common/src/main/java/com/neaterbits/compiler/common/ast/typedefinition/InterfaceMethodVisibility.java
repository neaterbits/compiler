package com.neaterbits.compiler.common.ast.typedefinition;

public enum InterfaceMethodVisibility implements InterfaceMethodModifier {
	PUBLIC;

	@Override
	public <T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onVisibility(this, param);
	}
}
