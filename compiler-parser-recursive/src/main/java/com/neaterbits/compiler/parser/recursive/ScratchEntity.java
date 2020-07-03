package com.neaterbits.compiler.parser.recursive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neaterbits.util.parse.ParserException;

public abstract class ScratchEntity<PART, TO_PROCESS, LIST> implements ScratchList<TO_PROCESS> {

    private final ScratchBuf<PART, TO_PROCESS, LIST, ?> buf;
    
    private final List<PART> partList;
    private int numPartElements;
    private boolean inUse;

    private int index;

    ScratchEntity(ScratchBuf<PART, TO_PROCESS, LIST, ?> buf) {
        
        this.buf = buf;
        
        // Allocate 100 elements, should always be enough or will cause exception
        this.partList = new ArrayList<>(100);
        this.numPartElements = 0;
    }
    
    abstract PART createPart();
    
    abstract TO_PROCESS getToProcess();
    
    abstract LIST getList();

    int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    final boolean isInUse() {
        return inUse;
    }

    final void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
    
    final PART get(int index) {
        return partList.get(index);
    }
    
    @Override
    public void complete(ProcessParts<TO_PROCESS> process) throws IOException, ParserException {
        
        try {
            try {
                process.processParts(getToProcess());
            }
            finally {
                clear();
                
                buf.free(this);
            }
        }
        finally {
            clear();
        }
    }

    final PART getOrCreate() {
        
        final PART part;
        
        if (numPartElements == partList.size()) {
            
            part = createPart();
            
            partList.add(part);
        }
        else {
            part = partList.get(numPartElements);
        }

        ++ numPartElements;
        
        return part;
    }
    
    final int getCount() {
        return numPartElements;
    }
    
    private void clear() {
        this.numPartElements = 0;
        this.inUse = false;
    }
}
