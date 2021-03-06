package dev.nimbler.compiler.ast.encoded;

public interface ASTBuffer extends ASTBufferRead, ASTBufferWrite {

    int getWritePos();
    
    void appendFrom(int pos, int size);
}
