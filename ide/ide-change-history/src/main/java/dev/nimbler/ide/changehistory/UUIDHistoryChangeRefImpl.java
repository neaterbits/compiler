package dev.nimbler.ide.changehistory;

import java.util.UUID;

final class UUIDHistoryChangeRefImpl extends UUIDReasonChangeRefImpl implements HistoryChangeRef {

    private final UUIDChangeRefImpl historyChangeRef;
    
    UUIDHistoryChangeRefImpl(
            UUID topLevelUUID,
            UUID subDirUUID,
            ChangeReason changeReason,
            UUIDChangeRefImpl historyChangeRef) {
        
        super(topLevelUUID, subDirUUID, changeReason);
        
        this.historyChangeRef = historyChangeRef;
    }

    @Override
    public ChangeRef getHistoryChangeRef() {
        return historyChangeRef;
    }
}
