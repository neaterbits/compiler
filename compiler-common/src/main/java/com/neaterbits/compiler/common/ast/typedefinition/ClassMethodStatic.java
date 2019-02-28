package com.neaterbits.compiler.common.ast.typedefinition;

public final class ClassMethodStatic implements ClassMethodModifier {

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onStatic(this, param);
	}
}
