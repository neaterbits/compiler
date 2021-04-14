package dev.nimbler.compiler.parser.recursive.cached.expressions;

import dev.nimbler.compiler.parser.recursive.cached.ScratchBuf;
import dev.nimbler.compiler.parser.recursive.cached.ScratchEntity;

final class CachedPrimariesImpl
    extends ScratchEntity<CachedPrimary, Primaries, PrimariesAllocator>
    implements Primaries, PrimariesAllocator {
    
    CachedPrimariesImpl(ScratchBuf<CachedPrimary, Primaries, PrimariesAllocator, ?> buf) {
        super(buf);
    }
    
    @Override
    public CachedPrimary getOrCreatePrimary() {
        return getOrCreate();
    }

    @Override
    protected CachedPrimary createPart() {

        return new CachedPrimary();
    }

    @Override
    protected Primaries getToProcess() {

        return this;
    }

    @Override
    protected PrimariesAllocator getList() {

        return this;
    }
}
