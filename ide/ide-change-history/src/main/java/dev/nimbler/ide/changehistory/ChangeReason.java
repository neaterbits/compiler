package dev.nimbler.ide.changehistory;

/**
 * Reason for change
 * 
 */

public enum ChangeReason {

    /**
     * A refactoring operation
     */
    REFACTOR(false),
    
    /**
     * A source file edit
     */
    EDIT(false),
    
    
    /**
     * History undo
     */
    UNDO(true),
    
    /**
     * History redo
     */
    REDO(true),
    
    /**
     * History move
     */
    MOVE(true);
    
    private final boolean historyMove;

    private ChangeReason(boolean historyMove) {
        this.historyMove = historyMove;
    }

    /**
     * Whether this is a history move
     * 
     * @return true if this is a history move
     */
    public boolean isHistoryMove() {
        return historyMove;
    }
}
