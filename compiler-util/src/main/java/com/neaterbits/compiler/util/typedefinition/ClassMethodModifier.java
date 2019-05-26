package com.neaterbits.compiler.util.typedefinition;

public interface ClassMethodModifier {

	<T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param);

}
