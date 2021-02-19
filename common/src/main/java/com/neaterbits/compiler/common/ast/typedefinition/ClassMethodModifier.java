package com.neaterbits.compiler.common.ast.typedefinition;

public interface ClassMethodModifier {

	<T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param);

}
