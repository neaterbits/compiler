package dev.nimbler.compiler.parser.java.recursive;

import com.neaterbits.util.parse.CharType;

final class JavaIdentifierPartCharType extends CharType {

    static final JavaIdentifierPartCharType INSTANCE = new JavaIdentifierPartCharType();

    private JavaIdentifierPartCharType() {
        
    }
    
    @Override
    protected boolean isOfType(char c) {
        return Character.isJavaIdentifierPart(c);
    }
}
