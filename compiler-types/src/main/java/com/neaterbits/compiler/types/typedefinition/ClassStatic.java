package com.neaterbits.compiler.types.typedefinition;

public final class ClassStatic implements ClassModifier {

	@Override
	public <T, R> R visit(ClassModifierVisitor<T, R> visitor, T param) {
		return visitor.onStatic(this, param);
	}
}
