package com.neaterbits.compiler.common.ast.typedefinition;

public final class InterfaceStatic implements InterfaceModifier {

	@Override
	public <T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param) {
		return visitor.onStatic(this, param);
	}
}
