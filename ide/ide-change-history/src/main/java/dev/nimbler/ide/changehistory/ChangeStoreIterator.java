package dev.nimbler.ide.changehistory;

interface ChangeStoreIterator {

    IteratorContinuation onChange(ReasonChangeRef changeRef, ChangeRef historyMoveChangeRef);
}
