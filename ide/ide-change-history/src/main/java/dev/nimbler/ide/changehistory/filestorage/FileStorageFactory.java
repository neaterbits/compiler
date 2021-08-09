package dev.nimbler.ide.changehistory.filestorage;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageFactory {

    FileStorage createPrevStateStorage(Path directory) throws IOException;
    
    FileStorage createEditsStorage(Path directory) throws IOException;

    FileRetrieval createPrevStateRetrieval(Path directory) throws IOException;
    
    FileRetrieval createEditsRetrieval(Path directory) throws IOException;

}
