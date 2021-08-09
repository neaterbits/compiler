package dev.nimbler.ide.changehistory;

import java.util.UUID;

final class UUIDHistoricChangeRefImpl extends UUIDReasonChangeRefImpl {

    private final UUIDChangeRefImpl historicChangeRef;
    
    UUIDHistoricChangeRefImpl(
            UUID topLevelUUID,
            UUID subDirUUID,
            ChangeReason changeReason,
            UUIDChangeRefImpl historicChangeRef) {
        
        super(topLevelUUID, subDirUUID, changeReason);
        
        this.historicChangeRef = historicChangeRef;
    }

    UUIDChangeRefImpl getHistoricChangeRef() {
        return historicChangeRef;
    }
}
