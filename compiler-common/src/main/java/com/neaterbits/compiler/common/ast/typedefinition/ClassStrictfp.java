package com.neaterbits.compiler.common.ast.typedefinition;

public final class ClassStrictfp implements ClassModifier {

	@Override
	public <T, R> R visit(ClassModifierVisitor<T, R> visitor, T param) {
		return visitor.onStrictFp(this, param);
	}
}
