package com.neaterbits.compiler.parser.recursive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

final class ScratchBuf<PART, TO_PROCESS, LIST, IMPL extends ScratchEntity<PART, TO_PROCESS, LIST>> {

    private final Function<ScratchBuf<PART, TO_PROCESS, LIST, ?>, IMPL> createEntity;
    private final List<IMPL> scratch;
    private int scratchInUse;

    ScratchBuf(Function<ScratchBuf<PART, TO_PROCESS, LIST, ?>, IMPL> createEntity) {
        
        Objects.requireNonNull(createEntity);

        this.createEntity = createEntity;
        this.scratch = new ArrayList<>();
    }

    LIST startScratchParts() {

        final int index;
        
        if (scratchInUse == scratch.size()) {
            
            final IMPL entity = createEntity.apply(this);
            
            entity.setInUse(true);
            
            scratch.add(entity);
            index = scratchInUse;
            entity.setIndex(index);
        }
        else {
            int found = -1;
            
            for (int i = 0; i < scratch.size(); ++ i) {
                final IMPL entity = scratch.get(i);
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
        
        return scratch.get(index).getList();
    }
    
    final void free(ScratchEntity<PART, TO_PROCESS, LIST> scratch) {
        
        -- scratchInUse;
    }
}
