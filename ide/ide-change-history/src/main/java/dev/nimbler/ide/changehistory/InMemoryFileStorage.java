package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Objects;

import dev.nimbler.ide.changehistory.filestorage.FileStorage;

final class InMemoryFileStorage extends InMemoryCloseable implements FileStorage {

    private final InMemoryFiles files;
    
    public InMemoryFileStorage(InMemoryFiles files) {

        Objects.requireNonNull(files);
        
        this.files = files;
    }

    @Override
    public OutputStream addFile(Path filePath) throws IOException {

        checkClosed();

        return files.addFile(filePath);
    }
}
