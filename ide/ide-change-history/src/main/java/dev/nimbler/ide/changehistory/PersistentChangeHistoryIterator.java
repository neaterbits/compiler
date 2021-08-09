package dev.nimbler.ide.changehistory;

/**
 * Iterator for history
 */

public interface PersistentChangeHistoryIterator {

    /**
     * Called for every historic edit
     * 
     * @param changeRef opaque reference to a change
     * @param historyMoveChangeRef opaque reference to history entry we have moved from
     *                             if change is historic move, that is {@link ChangeReason#isHistoryMove()} returns true
     *                             
     * 
     * @return {@link IteratorContinuation#CONTINUE} if continue iterating,
     *         {@link IteratorContinuation#EXIT} otherwise
     */
    IteratorContinuation onChange(ReasonChangeRef changeRef, ChangeRef historyMoveChangeRef);
    
}
