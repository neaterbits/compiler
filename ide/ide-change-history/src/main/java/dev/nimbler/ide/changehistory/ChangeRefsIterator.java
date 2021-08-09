package dev.nimbler.ide.changehistory;

/**
 * Quick iterator for persistent changes
 * for iterating all refs
 */

public interface ChangeRefsIterator {

    /**
     * Called for each change
     * 
     * @param changeRef the change reference
     */
    
    void onChange(ReasonChangeRef changeRef);
}
