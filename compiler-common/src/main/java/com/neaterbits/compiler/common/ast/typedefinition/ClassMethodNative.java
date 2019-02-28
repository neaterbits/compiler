package com.neaterbits.compiler.common.ast.typedefinition;

public final class ClassMethodNative implements ClassMethodModifier {

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return visitor.onNative(this, param);
	}
}
