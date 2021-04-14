package dev.nimbler.compiler.types.typedefinition;

public interface ConstructorModifierVisitor<T, R> {

	R onVisibility(ConstructorVisibility visibility, T param);
	
}
