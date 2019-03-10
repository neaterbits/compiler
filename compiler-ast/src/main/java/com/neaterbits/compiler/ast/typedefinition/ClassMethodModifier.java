package com.neaterbits.compiler.ast.typedefinition;

public interface ClassMethodModifier {

	<T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param);

}
