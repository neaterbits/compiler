package com.neaterbits.compiler.common.ast.typedefinition;

public interface MethodModifier {

	<T, R> R visit(MethodModifierVisitor<T, R> visitor, T param);

}
