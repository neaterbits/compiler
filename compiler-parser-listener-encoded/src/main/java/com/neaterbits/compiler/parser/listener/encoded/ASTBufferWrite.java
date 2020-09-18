package com.neaterbits.compiler.parser.listener.encoded;

import com.neaterbits.compiler.types.ParseTreeElement;

public interface ASTBufferWrite {

    void writeBoolean(boolean value);
    
    void writeByte(byte value);
    
    void writeShort(short value);
    
    void writeInt(int value);
    
    void writeLong(long value);
    
    void writeElementStart(ParseTreeElement element);

    void writeElementEnd(ParseTreeElement element);

    void writeLeafElement(ParseTreeElement element);

}
