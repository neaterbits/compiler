package com.neaterbits.compiler.types.typedefinition;

public final class ClassMethodStrictfp implements ClassMethodModifier {

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onStrictFp(this, param);
	}
}
