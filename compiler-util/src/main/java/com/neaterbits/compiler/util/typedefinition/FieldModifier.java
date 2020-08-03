package com.neaterbits.compiler.util.typedefinition;

public interface FieldModifier {

    public enum Type {
        VISIBILITY,
        MUTABILITY,
        STATIC,
        VOLATILE,
        TRANSIENT;
    }

	<T, R> R visit(FieldModifierVisitor<T, R> visitor, T param);
	
}
