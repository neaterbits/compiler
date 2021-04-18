package dev.nimbler.compiler.parser.java.recursive;

import org.jutils.parse.CharType;

final class JavaIdentifierStartCharType extends CharType {

    static final JavaIdentifierStartCharType INSTANCE = new JavaIdentifierStartCharType();
    
    private JavaIdentifierStartCharType() {
        
    }
    
    @Override
    protected boolean isOfType(char c) {
        
        return Character.isJavaIdentifierStart(c);
    }
}
