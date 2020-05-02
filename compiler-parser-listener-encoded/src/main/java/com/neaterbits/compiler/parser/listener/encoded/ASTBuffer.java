package com.neaterbits.compiler.parser.listener.encoded;

public interface ASTBuffer extends ASTBufferRead, ASTBufferWrite {

    int getWritePos();
}
