package dev.nimbler.compiler.parser.recursive.cached;

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

    protected ScratchEntity(ScratchBuf<PART, TO_PROCESS, LIST, ?> buf) {
        
        this.buf = buf;
        
        // Allocate 100 elements, should always be enough or will cause exception
        this.partList = new ArrayList<>(100);
        this.numPartElements = 0;
    }
    
    protected abstract PART createPart();
    
    protected abstract TO_PROCESS getToProcess();
    
    protected abstract LIST getList();

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
    
    protected final PART get(int index) {
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

    protected final PART getOrCreate() {
        
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
    
    protected final int getCount() {
        return numPartElements;
    }
    
    final void clear() {
        this.numPartElements = 0;
        this.inUse = false;
    }
}
