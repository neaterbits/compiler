package com.neaterbits.compiler.parser.listener.encoded;

import java.util.Objects;

import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;

final class StringASTBuffer {

    private final Tokenizer tokenizer;

    private final ASTBuffer astBuffer;
    
    private final MapStringStorageBuffer stringBuffer;

    public StringASTBuffer(Tokenizer tokenizer) {
        
        Objects.requireNonNull(tokenizer);
        
        this.tokenizer = tokenizer;
        
        this.astBuffer = new ASTBufferImpl();
        this.stringBuffer = new MapStringStorageBuffer();
    }

    private int getStringIndex(long stringRef) {
        
        // TODO fix allocation
        return tokenizer.addToBuffer(stringBuffer, stringRef);
    }
    
    void writeStringRef(long stringRef) {
        
        astBuffer.writeInt(getStringIndex(stringRef));
    }

    void writeNoStringRef() {
        
        astBuffer.writeInt(-1);
    }

    void writeContextRef(int contextRef) {
        
        astBuffer.writeInt(contextRef);
    }

    void writeNoContextRef() {
        
        astBuffer.writeInt(-1);
    }

    int getParseTreeRef() {
        
        return astBuffer.getWritePos();
    }

    void writeElementStart(ParseTreeElement element) {
        
        astBuffer.writeElementStart(element);
    }

    void writeElementEnd(ParseTreeElement element) {
        
        astBuffer.writeElementEnd(element);
    }

    void writeLeafElement(ParseTreeElement element) {
        
        astBuffer.writeLeafElement(element);
    }
    
    void writeBoolean(boolean value) {
        
        astBuffer.writeBoolean(value);
    }
    
    ASTBufferRead getASTReadBuffer() {
        return astBuffer;
    }

    MapStringStorageBuffer getStringBuffer() {
        return stringBuffer;
    }
}
