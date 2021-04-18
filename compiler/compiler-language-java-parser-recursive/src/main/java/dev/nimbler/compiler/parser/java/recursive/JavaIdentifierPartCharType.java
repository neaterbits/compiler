package dev.nimbler.compiler.parser.java.recursive;

import org.jutils.parse.CharType;

final class JavaIdentifierPartCharType extends CharType {

    static final JavaIdentifierPartCharType INSTANCE = new JavaIdentifierPartCharType();

    private JavaIdentifierPartCharType() {
        
    }
    
    @Override
    protected boolean isOfType(char c) {
        return Character.isJavaIdentifierPart(c);
    }
}
