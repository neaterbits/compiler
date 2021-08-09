package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.FileRetrieval;

final class InMemoryChangeStore extends BaseChangeStore implements ChangeStore {

    private volatile boolean closed;
    
    private final boolean close;
    
    private final List<InMemoryChange> changes;

    InMemoryChangeStore() {
        
        this(true);
    }

    InMemoryChangeStore(boolean close) {

        this.close = close;

        this.changes = new ArrayList<>();

        this.closed = false;
    }

    private void checkClosed() {

        if (closed) {
            throw new IllegalStateException();
        }
    }

    @Override
    public ChangeHistoryStorageFactory prepare(SourceFileComplexChange change) throws IOException {
        
        checkClosed();
        
        Objects.requireNonNull(change);

        final InMemoryChange inMemoryChange = new InMemoryChange(change);
        
        changes.add(inMemoryChange);
        
        return inMemoryChange;
    }

    @Override
    public void iterateChanges(boolean chronological, ChangeStoreIterator iterator) throws IOException {

        checkClosed();

        iterate(changes, chronological, iterator);
    }

    @Override
    public FileRetrieval createPrevStateChangeInput(ChangeRef changeRef) throws IOException {
        
        throw new UnsupportedOperationException();
    }

    @Override
    public FileRetrieval createEditsChangeInput(ChangeRef changeRef) throws IOException {

        checkClosed();

        final InMemoryChange change = (InMemoryChange)changeRef;
        
        return new InMemoryFileRetrieval(change.getInMemoryFileStorage());
    }

    @Override
    public void close() throws Exception {

        checkClosed();
        
        if (close) {
            this.closed = true;
        }
    }
}
