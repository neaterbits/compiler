package com.neaterbits.compiler.parser.recursive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ScratchEntity<PART, TO_PROCESS> {

    private final List<PART> partList;
    private int numPartElements;
    private boolean inUse;

    ScratchEntity() {
        // Allocate 100 elements, should always be enough or will cause exception
        this.partList = new ArrayList<>(100);
        this.numPartElements = 0;
    }
    
    abstract TO_PROCESS getToProcess();

    final boolean isInUse() {
        return inUse;
    }

    final void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
    
    final PART get(int index) {
        return partList.get(index);
    }
    
    final void add(Supplier<PART> create, Consumer<PART> init) {
        
        if (numPartElements == partList.size()) {
            
            partList.add(create.get());
        }
        else {
            init.accept(partList.get(numPartElements));
        }

        ++ numPartElements;
    }
    
    final int getCount() {
        return numPartElements;
    }
    
    final void clear() {
        this.numPartElements = 0;
        this.inUse = false;
    }
}
