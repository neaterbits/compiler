package dev.nimbler.ide.changehistory;

import java.nio.file.Path;

/**
 * A {@link ChangeHistory} implementation that stores in-memory eg. for unit tests
 * or for undo/redo per keypress or words where we do not care to persist to disk.
 *  
 */
public final class InMemoryChangeHistory extends BaseChangeHistory {

    public InMemoryChangeHistory(Path ideBasePath) {
        this(ideBasePath, new InMemoryChangeStore());
    }

    public InMemoryChangeHistory(Path ideBasePath, InMemoryChangeStore changeStore) {
        super(ideBasePath, changeStore, false);
    }
}
