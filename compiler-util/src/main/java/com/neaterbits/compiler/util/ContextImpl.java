package com.neaterbits.compiler.util;

public class ContextImpl implements Context {

    private final int startOffset;
    private final int endOffset;
    
    public ContextImpl(int startOffset, int endOffset) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }
    
    public ContextImpl(ContextImpl context) {
        this(context.getStartOffset(), context.getEndOffset());
    }
    
    @Override
    public final int getStartOffset() {
        return startOffset;
    }

    @Override
    public final int getEndOffset() {
        return endOffset;
    }
}
