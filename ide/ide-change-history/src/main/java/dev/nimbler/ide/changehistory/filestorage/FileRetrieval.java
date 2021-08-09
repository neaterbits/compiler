package dev.nimbler.ide.changehistory.filestorage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileRetrieval extends AutoCloseable {

    /**
     * Retrieve a file from the file storage, or null if file not found
     * 
     * @param filePath path of the file relative to IDE root
     * 
     * @return an open {@link InputStream} that one can read the file contents from, or null if file not found
     * 
     * @throws IOException if failed to open input
     */
    InputStream retrieveFile(Path filePath) throws IOException;

}
