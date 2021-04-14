package dev.nimbler.compiler.types.typedefinition;

public interface VariableModifier {
	
    enum Type {
        MUTABILITY
    }
    
	<T, R> R visit(VariableModifierVisitor<T, R> visitor, T param);

}
