package com.neaterbits.compiler.parser.listener.encoded;

public interface ASTBuffer extends ASTBufferRead, ASTBufferWrite {

    int getWritePos();
    
    void appendFrom(int pos, int size);
}
