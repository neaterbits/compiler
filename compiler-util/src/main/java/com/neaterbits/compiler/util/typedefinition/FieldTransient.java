package com.neaterbits.compiler.util.typedefinition;

public final class FieldTransient implements FieldModifier {

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onTransient(this, param);
	}
}
