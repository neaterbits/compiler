package com.neaterbits.compiler.parser.recursive.cached.expressions;

public interface PrimariesAllocator {

    CachedPrimary getOrCreatePrimary();
}
