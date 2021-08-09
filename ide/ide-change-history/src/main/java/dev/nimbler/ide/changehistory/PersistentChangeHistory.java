package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Implementation of {@link ChangeHistory} that persists to file system.
 */

public final class PersistentChangeHistory
        extends BaseChangeHistory
        implements ChangeHistory {

    /**
     * Construct a new {@link PersistentChangeHistory}
     * 
     * @param changeHistoryStorageBasePath base filesystem path to store the edit history
     * @param ideBasePath the base path to the IDE projects,
     *        for computing relative path to source files
     * 
     * @throws IOException if failed to initialize
     */

    public PersistentChangeHistory(Path changeHistoryStorageBasePath, Path ideBasePath)
                throws IOException {

        super(ideBasePath, new UUIDChangeStore(changeHistoryStorageBasePath, 1000), true);
    }
}
