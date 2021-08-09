package dev.nimbler.ide.changehistory;

import java.io.InputStream;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

/**
 * Listener interface for applying an undo operation from change history
 */
public interface ApplyUndoListener {

    /**
     * Write a file to file system
     * 
     * @param file the file to write
     * @param fileContents the file contents to write
     */
    void writeFile(SourceFileResourcePath file, InputStream fileContents);
    
    /**
     * Move a file from the given source path to the given destination path
     * 
     * @param sourcePath source path
     * @param destinationPath destination path
     */
    void moveFile(SourceFileResourcePath sourcePath, SourceFileResourcePath destinationPath);
}
