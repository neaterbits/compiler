package dev.nimbler.compiler.types.typedefinition;

public interface InterfaceMethodModifier {

	<T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param);
	
}
