package com.neaterbits.compiler.util.typedefinition;

public final class ClassMethodStrictfp implements ClassMethodModifier {

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onStrictFp(this, param);
	}
}
