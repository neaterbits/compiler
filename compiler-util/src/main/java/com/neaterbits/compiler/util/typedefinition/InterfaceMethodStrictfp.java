package com.neaterbits.compiler.util.typedefinition;

public final class InterfaceMethodStrictfp implements InterfaceMethodModifier {

	@Override
	public <T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onStrictfp(this, param);
	}
}

