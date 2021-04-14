package com.neaterbits.compiler.types.typedefinition;

public interface ClassModifier {

    public enum Type {
        VISIBILITY,
        SUBCLASSING;
    }
    
	<T, R> R visit(ClassModifierVisitor<T, R> visitor, T param);
}
