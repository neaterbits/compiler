package com.neaterbits.compiler.parser.java;

import com.neaterbits.util.parse.CharType;

final class JavaIdentifierStartCharType extends CharType {

    static final JavaIdentifierStartCharType INSTANCE = new JavaIdentifierStartCharType();
    
    private JavaIdentifierStartCharType() {
        
    }
    
    @Override
    protected boolean isOfType(char c) {
        
        return Character.isJavaIdentifierStart(c);
    }
}
