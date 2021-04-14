package dev.nimbler.compiler.resolver.build;

public final class CompilerOptions {

    private final boolean addTokenRefs;

    public CompilerOptions(boolean addTokenRefs) {
        this.addTokenRefs = addTokenRefs;
    }

    public boolean isAddTokenRefsEnabled() {
        return addTokenRefs;
    }
}
