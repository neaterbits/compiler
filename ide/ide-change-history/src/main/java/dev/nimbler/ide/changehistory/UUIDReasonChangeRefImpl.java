package dev.nimbler.ide.changehistory;

import java.util.Objects;
import java.util.UUID;

/**
 * {@link UUIDChangeStore} implementation of {@link ReasonChangeRef}
 */

class UUIDReasonChangeRefImpl extends UUIDChangeRefImpl implements ReasonChangeRef {

    private final ChangeReason change;
    
    UUIDReasonChangeRefImpl(UUID topLevelUUID, UUID subDirUUID, ChangeReason changeReason) {

        super(topLevelUUID, subDirUUID);
        
        Objects.requireNonNull(changeReason);
        
        this.change = changeReason;
    }

    @Override
    public final ChangeReason getChangeReason() {
        return change;
    }
}
