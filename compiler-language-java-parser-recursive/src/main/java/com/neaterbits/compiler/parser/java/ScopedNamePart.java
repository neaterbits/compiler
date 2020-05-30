package com.neaterbits.compiler.parser.java;

import com.neaterbits.compiler.util.Context;

final class ScopedNamePart {

    private final Context context;
    private final long part;
    
    ScopedNamePart(Context context, long part) {
        this.context = context;
        this.part = part;
    }

    Context getContext() {
        return context;
    }

    long getPart() {
        return part;
    }
}
