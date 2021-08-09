package dev.nimbler.ide.changehistory.changeoutput;

import java.io.IOException;

import dev.nimbler.ide.changehistory.filestorage.FileStorage;

public interface ChangeHistoryStorageFactory extends AutoCloseable {

    FileStorage createPrevStateChangeOutput() throws IOException;
    
    FileStorage createEditsChangeOutput() throws IOException;
}
