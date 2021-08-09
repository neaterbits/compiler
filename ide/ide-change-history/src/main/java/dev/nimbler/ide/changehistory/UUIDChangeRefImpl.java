package dev.nimbler.ide.changehistory;

import java.util.Objects;
import java.util.UUID;

class UUIDChangeRefImpl implements ChangeRef {

    private final UUID topLevelUUID;
    private final UUID subDirUUID;
    
    UUIDChangeRefImpl(UUID topLevelUUID, UUID subDirUUID) {

        Objects.requireNonNull(topLevelUUID);
        Objects.requireNonNull(subDirUUID);

        this.topLevelUUID = topLevelUUID;
        this.subDirUUID = subDirUUID;
    }

    final UUID getTopLevelUUID() {
        return topLevelUUID;
    }

    final UUID getSubDirUUID() {
        return subDirUUID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subDirUUID, topLevelUUID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof UUIDChangeRefImpl))
            return false;
        final UUIDChangeRefImpl other = (UUIDChangeRefImpl) obj;
        return Objects.equals(subDirUUID, other.subDirUUID) && Objects.equals(topLevelUUID, other.topLevelUUID);
    }
}
