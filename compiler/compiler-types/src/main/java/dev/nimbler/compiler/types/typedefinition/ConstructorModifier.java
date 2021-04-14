package dev.nimbler.compiler.types.typedefinition;

public interface ConstructorModifier {

	<T, R> R visit(ConstructorModifierVisitor<T, R> visitor, T param);
	
}
