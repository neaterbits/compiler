package com.neaterbits.compiler.util.parse;

public final class NamePart {

    private int context;
    private long part;
    
    public NamePart(int context, long part) {
        init(context, part);
    }
    
    // TODO make immutable?
    public void init(int context, long part) {
        this.context = context;
        this.part = part;
    }

    public int getContext() {
        return context;
    }

    public long getPart() {
        return part;
    }
}
