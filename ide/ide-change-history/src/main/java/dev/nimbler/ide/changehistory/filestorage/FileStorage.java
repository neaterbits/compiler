package dev.nimbler.ide.changehistory.filestorage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public interface FileStorage extends AutoCloseable {

    /**
     * Add a file to the file storage
     * 
     * @param filePath path of the file relative to IDE root
     * 
     * @return an open {@link OutputStream} that one can write to
     * 
     * @throws IOException if failed to open output
     */
    OutputStream addFile(Path filePath) throws IOException;

}
