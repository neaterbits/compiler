package com.neaterbits.compiler.parser.listener.encoded;

import com.neaterbits.compiler.util.model.ParseTreeElement;

public interface ASTBufferRead {

    boolean getBoolean(int index);
    
    byte getByte(int index);
    
    short getShort(int index);
    
    int getInt(int index);
    
    long getLong(int index);
    
    ParseTreeElement getParseTreeElement(int index);
}
