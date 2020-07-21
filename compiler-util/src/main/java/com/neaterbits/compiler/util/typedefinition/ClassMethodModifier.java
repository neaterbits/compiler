package com.neaterbits.compiler.util.typedefinition;

public interface ClassMethodModifier {

    public enum Type {
        VISIBILITY,
        STATIC,
        OVERRIDE
    }
    
	<T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param);

}
