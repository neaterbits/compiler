package com.neaterbits.compiler.common.ast.typedefinition;

public final class InterfaceMethodAbstract implements InterfaceMethodModifier {

	@Override
	public <T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onAbstract(this, param);
	}
}
