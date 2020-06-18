package com.neaterbits.compiler.parser.java;

final class ScopedNamePart {

    private final int context;
    private final long part;
    
    ScopedNamePart(int context, long part) {
        this.context = context;
        this.part = part;
    }

    int getContext() {
        return context;
    }

    long getPart() {
        return part;
    }
}
