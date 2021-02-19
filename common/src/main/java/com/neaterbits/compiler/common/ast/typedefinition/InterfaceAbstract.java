package com.neaterbits.compiler.common.ast.typedefinition;

public final class InterfaceAbstract implements InterfaceModifier {

	@Override
	public <T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param) {
		return visitor.onAbstract(this, param);
	}
}
