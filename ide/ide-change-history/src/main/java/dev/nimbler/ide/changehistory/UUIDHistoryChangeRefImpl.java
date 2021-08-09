package dev.nimbler.ide.changehistory;

import java.util.UUID;

final class UUIDHistoryChangeRefImpl extends UUIDReasonChangeRefImpl {

    private final UUIDChangeRefImpl historyChangeRef;
    
    UUIDHistoryChangeRefImpl(
            UUID topLevelUUID,
            UUID subDirUUID,
            ChangeReason changeReason,
            UUIDChangeRefImpl historyChangeRef) {
        
        super(topLevelUUID, subDirUUID, changeReason);
        
        this.historyChangeRef = historyChangeRef;
    }

    UUIDChangeRefImpl getHistoryChangeRef() {
        return historyChangeRef;
    }
}
