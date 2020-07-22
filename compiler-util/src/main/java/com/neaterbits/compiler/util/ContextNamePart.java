package com.neaterbits.compiler.util;

public final class ContextNamePart {
    
    private final Context context;
    private final long name;

    public ContextNamePart(Context context, long name) {
        this.context = context;
        this.name = name;
    }

    public Context getContext() {
        return context;
    }

    public long getName() {
        return name;
    }
}
