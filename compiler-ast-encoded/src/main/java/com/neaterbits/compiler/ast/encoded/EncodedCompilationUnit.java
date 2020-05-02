package com.neaterbits.compiler.ast.encoded;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.ImmutableContext;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.compiler.parser.listener.encoded.ASTBufferRead;

public final class EncodedCompilationUnit {

    private final ASTBufferRead astBuffer;
    private final IntKeyIntValueHash parseTreeRefToContextHash;
    private final ASTBufferRead contexts;

    private final TypeName [] indexToTypeName;
    
    private final MapStringStorageBuffer stringBuffer;

    public EncodedCompilationUnit(
            ASTBufferRead astBuffer,
            ASTBufferRead contextBuffer,
            IntKeyIntValueHash parseTreeRefToContextHash,
            Map<TypeName, Integer> typeNameToIndex,
            MapStringStorageBuffer stringBuffer) {

        Objects.requireNonNull(astBuffer);
        Objects.requireNonNull(parseTreeRefToContextHash);
        Objects.requireNonNull(contextBuffer);

        this.astBuffer = astBuffer;
        this.parseTreeRefToContextHash = parseTreeRefToContextHash;
        this.contexts = contextBuffer;
        this.stringBuffer = stringBuffer;
        
        this.indexToTypeName = new TypeName[typeNameToIndex.size()];
        
        for (Map.Entry<TypeName, Integer> entry : typeNameToIndex.entrySet()) {
            
            final int index = entry.getValue();
            
            if (indexToTypeName[index] != null) {
                throw new IllegalStateException();
            }

            indexToTypeName[index] = entry.getKey();
        }
    }

    public ASTBufferRead getBuffer() {
        return astBuffer;
    }
    
    public ParseTreeElement getParseTreeElement(int parseTreeRef) {
        return astBuffer.getParseTreeElement(parseTreeRef);
    }
    
    public String getString(int astBufferOffset) {
        
        final int stringRef = astBuffer.getInt(astBufferOffset);
        
        return stringBuffer.getString(stringRef);
    }

    public TypeName getTypeName(int astBufferOffset) {
        
        final int typeIndex = astBuffer.getInt(astBufferOffset);
    
        return indexToTypeName[typeIndex];
    }
    
    private static final int INDEX_START_LINE       = 0;
    private static final int INDEX_START_POS_IN_LINE = 4;
    private static final int INDEX_START_OFFSET     = 8;
    private static final int INDEX_END_LINE         = 12;
    private static final int INDEX_END_POS_IN_LINE  = 16;
    private static final int INDEX_END_OFFSET       = 20;
    private static final int INDEX_TEXT           = 24;
    
    public Context getContext(int parseTreeRef) {
        
        final int index = parseTreeRefToContextHash.get(parseTreeRef);
        
        final ImmutableContext context = new ImmutableContext(
                null,
                contexts.getInt(index + INDEX_START_LINE),
                contexts.getInt(index + INDEX_START_POS_IN_LINE),
                contexts.getInt(index + INDEX_START_OFFSET),
                contexts.getInt(index + INDEX_END_LINE),
                contexts.getInt(index + INDEX_END_POS_IN_LINE),
                contexts.getInt(index + INDEX_END_OFFSET),
                getTokenStringFromIndex(index));
        
        return context;
    }
    
    public String getTokenString(int parseTreeRef) {
        
        final int index = parseTreeRefToContextHash.get(parseTreeRef);
        
        return getTokenStringFromIndex(index);
    }
    
    private String getTokenStringFromIndex(int index) {
        
        final int stringRef = contexts.getInt(index + INDEX_TEXT);
        
        return stringBuffer.getString(stringRef);
    }
    
    public int getTokenOffset(int parseTreeRef) {
        
        final int index = parseTreeRefToContextHash.get(parseTreeRef);

        return contexts.getInt(index + INDEX_START_OFFSET);
    }
    
    public int getTokenLength(int parseTreeRef) {

        final int index = parseTreeRefToContextHash.get(parseTreeRef);

        return    contexts.getInt(index + INDEX_END_OFFSET)
                - contexts.getInt(index + INDEX_START_OFFSET)
                + 1;
    }
}
