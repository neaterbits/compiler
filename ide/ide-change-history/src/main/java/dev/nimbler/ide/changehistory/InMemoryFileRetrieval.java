package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

import dev.nimbler.ide.changehistory.filestorage.FileRetrieval;

final class InMemoryFileRetrieval extends InMemoryCloseable implements FileRetrieval {

    private final InMemoryFiles inMemoryFiles;

    InMemoryFileRetrieval(InMemoryFiles inMemoryFiles) {
        
        Objects.requireNonNull(inMemoryFiles);

        this.inMemoryFiles = inMemoryFiles;
    }

    @Override
    public InputStream retrieveFile(Path filePath) throws IOException {

        checkClosed();
        
        return inMemoryFiles.retrieveFile(filePath);
    }
}
