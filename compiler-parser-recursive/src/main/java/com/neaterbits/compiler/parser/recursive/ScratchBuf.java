package com.neaterbits.compiler.parser.recursive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.neaterbits.util.parse.ParserException;

final class ScratchBuf<PART, TO_PROCESS, T extends ScratchEntity<PART, TO_PROCESS>> {

    private final Supplier<T> createEntity;
    private final List<T> scratch;
    private int scratchInUse;

    ScratchBuf(Supplier<T> createEntity) {
        
        Objects.requireNonNull(createEntity);

        this.createEntity = createEntity;
        this.scratch = new ArrayList<>();
    }

    int startScratchParts() {

        final int index;
        
        if (scratchInUse == scratch.size()) {
            
            final T entity = createEntity.get();
            
            entity.setInUse(true);
            
            scratch.add(entity);
            index = scratchInUse;
        }
        else {
            int found = -1;
            
            for (int i = 0; i < scratch.size(); ++ i) {
                final T entity = scratch.get(i);
                if (!entity.isInUse()) {
                    found = i;
                    entity.setInUse(true);
                    break;
                }
            }
            
            if (found == -1) {
                throw new IllegalStateException();
            }
            
            index = found;
        }
        
        if (scratch.get(index).getCount() > 0) {
            throw new IllegalStateException();
        }
        
        ++ scratchInUse;
        
        return index;
    }

    protected final void addScratchPart(int index, Supplier<PART> create, Consumer<PART> init) {
        
        final T entity = scratch.get(index);
        
        if (!entity.isInUse()) {
            throw new IllegalStateException();
        }

        entity.add(create, init);
    }
    
    @FunctionalInterface
    protected interface ProcessParts<T> {
        
        void processParts(T parts) throws IOException, ParserException;
    }

    protected final void completeScratchParts(int index, ProcessParts<TO_PROCESS> process) throws IOException, ParserException {
        
        if (scratchInUse < 1) {
            throw new IllegalStateException();
        }
        
        if (index != scratchInUse - 1) {
            throw new IllegalArgumentException();
        }
        
        final T entity = scratch.get(index);

        try {
            process.processParts(entity.getToProcess());
        }
        finally {
            entity.clear();
            
            -- scratchInUse;
        }
    }
}
