package dev.nimbler.ide.changehistory;


import java.io.IOException;

import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.FileRetrieval;

/**
 * Local interface for abstraction of history change storage
 */

interface ChangeStore extends AutoCloseable {

    /**
     * Prepare for storing a history change
     * 
     * @param change information about the source file changes
     * 
     * @return a {@link ChangeHistoryStorageFactory} for creating access objects for storing
     * 
     * @throws IOException if failed to prepare storage
     */
    ChangeHistoryStorageFactory prepare(SourceFileComplexChange change) throws IOException;

    /**
     * Iterate all changes
     * 
     * @param chronological true if iterating in chronological order
     * @param iterator a {@link ChangeStoreIterator} called for each change
     * 
     * @throws IOException if failed to iterate storage
     */
    void iterateChanges(
            boolean chronological,
            ChangeStoreIterator iterator) throws IOException;
    
    /**
     * Get file retrieval from a {@link ChangeRef}
     * 
     * @param changeRef a {@link ChangeRef} from iterating history
     * 
     * @return a {@link FileRetrieval} for retrieving files
     * 
     * @throws IOException thrown if failed to initialize file retrieval
     */
    FileRetrieval createPrevStateChangeInput(ChangeRef changeRef) throws IOException;
    
    /**
     * Get file retrieval from a {@link ChangeRef}
     * 
     * @param changeRef a {@link ChangeRef} from iterating history
     * 
     * @return a {@link FileRetrieval} for retrieving files
     * 
     * @throws IOException thrown if failed to initialize file retrieval
     */
    FileRetrieval createEditsChangeInput(ChangeRef changeRef) throws IOException;
}
