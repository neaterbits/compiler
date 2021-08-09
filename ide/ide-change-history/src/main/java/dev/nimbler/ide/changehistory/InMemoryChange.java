package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.util.Objects;

import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.FileStorage;

final class InMemoryChange
    extends InMemoryCloseable
    implements ChangeHistoryStorageFactory, HistoryChangeRef {

    private final SourceFileComplexChange sourceFileComplexChange;
    private final InMemoryFiles inMemoryFiles;
    
    InMemoryChange(SourceFileComplexChange sourceFileComplexChange) {
        
        Objects.requireNonNull(sourceFileComplexChange);
        
        this.sourceFileComplexChange = sourceFileComplexChange;
        this.inMemoryFiles = new InMemoryFiles();
    }

    InMemoryFiles getInMemoryFileStorage() {
        return inMemoryFiles;
    }

    @Override
    public FileStorage createPrevStateChangeOutput() throws IOException {

        checkClosed();

        throw new UnsupportedOperationException();
    }

    @Override
    public FileStorage createEditsChangeOutput() throws IOException {

        checkClosed();

        return new InMemoryFileStorage(inMemoryFiles);
    }

    // HistoryChangeRef
    @Override
    public ChangeReason getChangeReason() {

        return sourceFileComplexChange.getChangeReason();
    }

    @Override
    public ChangeRef getHistoryChangeRef() {
        return sourceFileComplexChange.getHistoryRef();
    }
}