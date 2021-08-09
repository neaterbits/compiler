package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.jutils.coll.MapOfList;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.model.text.TextEdit;

/**
 * Main interface for accessing project change history
 */

public interface ChangeHistory {

    /**
     * Add a trivial text edit (e.g. manual edit to a source file) to change history.
     * 
     * @param sourceFile the edited source file
     * @param textEdit the {@link TextEdit} representing the edit
     * 
     * @throws IOException thrown if failed to store the change history
     */
    void addTextEdit(SourceFileResourcePath sourceFile, TextEdit textEdit) throws IOException;

    /**
     * Add a refactor operation (e.g. rename a class) to change history.
     * 
     * @param removeFiles any removed files, or null
     * @param moveFiles any moved files, or null
     * @param edits any edits (e.g. updates in files referencing a renamed class)
     * 
     * @throws IOException thrown if failed to store the change history
     */
    void addRefactorChange(
            List<SourceFileResourcePath> removeFiles,
            Map<SourceFileResourcePath, SourceFileResourcePath> moveFiles,
            MapOfList<SourceFileResourcePath, TextEdit> edits) throws IOException;
    
    /**
     * Add a history move operation to change history
     *
     * @param changeReason the reason for a change, {@link ChangeReason#isHistoryMove() must return true}
     * @param movedToRef reference to the historic change we are moving to
     * @param addFiles any added files, or null
     * @param removeFiles any removed files, or null
     * @param moveFiles any moved files, or null
     * @param edits any edits (e.g. updates in files referencing a renamed class)
     * 
     * @throws IOException thrown if failed to store the change history
     */
    void addHistoryMoveChange(
            ChangeReason changeReason,
            ChangeRef movedToRef,
            List<SourceFileResourcePath> addFiles,
            List<SourceFileResourcePath> removeFiles,
            Map<SourceFileResourcePath, SourceFileResourcePath> moveFiles,
            MapOfList<SourceFileResourcePath, TextEdit> edits) throws IOException;
    
    /**
     * Iterate all changes
     * 
     * @param chronological true if iterating in chronological order
     * @param iterator a {@link ChangeHistoryIterator} called for each change
     * 
     * @throws IOException if failed to iterate storage
     */
    void iterateChanges(
            boolean chronological,
            ChangeHistoryIterator iterator) throws IOException;

    default void iterateAllChanges(
            boolean chronological,
            ChangeRefsIterator iterator) throws IOException {

        iterateChanges(chronological, (ref, historicRef) -> {

            iterator.onChange(ref);

            return IteratorContinuation.CONTINUE;
        });
    }

    /**
     * Retrieve a historical change
     * 
     * @param changeRef reference to the change
     * 
     */
    HistoricChange getHistoricChange(ReasonChangeRef changeRef) throws IOException;

    /**
     * Deserialize a prev state file
     * 
     * @param changeRef reference to change
     * @param path path to file, either removed file, source of move or edited file from {@link HistoricChange}
     * @param outputStream stream to write file to
     * 
     * @throws IOException if failed to read or write file
     */
    
    void deserializePrevStateFile(
            ChangeRef changeRef,
            Path path,
            OutputStream outputStream) throws IOException;
    
}
