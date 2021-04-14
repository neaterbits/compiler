package dev.nimbler.compiler.util.parse;

public final class NamePart {

    private int context;
    private long part;
    
    public NamePart() {
        
    }

    public NamePart(int context, long part) {
        init(context, part);
    }
    
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
