package com.neaterbits.compiler.util.typedefinition;

public interface ClassMethodModifier {

    public enum Type {
        STATIC
    }
    
	<T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param);

}
