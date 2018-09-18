package com.neaterbits.compiler.common.ast.typedefinition;

public final class FieldStatic implements FieldModifier {

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onStatic(this, param);
	}
}
