package com.neaterbits.compiler.util.typedefinition;

public final class FieldVolatile implements FieldModifier {

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onVolatile(this, param);
	}
}
