package com.neaterbits.compiler.util;

public final class MutableContext implements Context {

    private int startOffset;
    private int endOffset;
    
    public MutableContext() {

    }

    public MutableContext(int startOffset, int endOffset) {

        init(startOffset, endOffset);
    }

    public void init(Context context) {
        init(context.getStartOffset(), context.getEndOffset());
    }

    public void init(int startOffset, int endOffset) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
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
