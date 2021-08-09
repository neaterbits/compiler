package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.xml.stream.XMLStreamException;

import org.jutils.IOUtils;
import org.jutils.coll.MapOfList;

import dev.nimbler.ide.changehistory.filestorage.FileRetrieval;
import dev.nimbler.ide.model.text.TextEdit;

/**
 * Algorithm for deserializing a change, kept in separate helper class
 */

class ComplexChangeDeserializationHelper {

    static PathComplexChange deserialize(ChangeStore changeStore, ReasonChangeRef changeRef) throws IOException {
        
        final PathComplexChange complexChange;

        final ChangeReason changeReason = changeRef.getChangeReason();

        try (FileRetrieval changeInput = changeStore.createEditsChangeInput(changeRef)) {

            final MapOfList<Path, TextEdit> edits = readEdits(changeInput);
            
            switch (changeReason) {
            case REFACTOR:
                complexChange = deserialize(
                        changeInput,
                        changeReason,
                        edits,
                        ChangeSerializationPaths.REFACTOR_XML,
                        RefactorDeserializationHelper::deserializeRefactorFile);
                break;
                
            case MOVE:
            case UNDO:
            case REDO:
                complexChange = deserialize(
                        changeInput,
                        changeReason,
                        edits,
                        ChangeSerializationPaths.HISTORY_MOVE_XML,
                        RefactorDeserializationHelper::deserializeHistoryMoveFile);
                break;
                
            case EDIT:
                complexChange = new PathComplexChange(changeReason, null, null, null, edits);
                break;
                
            default:
                throw new IllegalStateException();
            }
        }
        catch (Exception ex) {
            throw new IOException("Exception while closing change input", ex);
        }
        
        return complexChange;
    }
    
    @FunctionalInterface
    interface Deserializer {
        
        PathComplexChange deserialize(
            InputStream inputStream,
            ChangeReason change,
            MapOfList<Path, TextEdit> edits)

                    throws IOException, XMLStreamException;
    }
    
    private static PathComplexChange deserialize(
            FileRetrieval changeInput,
            ChangeReason change,
            MapOfList<Path, TextEdit> edits,
            Path xmlFilePath,
            Deserializer deserializer) throws IOException, XMLStreamException {
        
        final PathComplexChange complexChange;
        
        final InputStream refactorStream = changeInput.retrieveFile(xmlFilePath);
        
        if (refactorStream != null) {
            try {
                complexChange = deserializer.deserialize(
                        refactorStream,
                        change,
                        edits);
            }
            finally {
                try {
                    refactorStream.close();
                } catch (IOException ex) {
                    throw new IOException("Exception while closing refactor file", ex);
                }
            }
        }
        else {
            complexChange = new PathComplexChange(change, null, null, null, edits);
        }
        
        return complexChange;
    }
    
    static void deserializePrevStateFile(
            ChangeStore changeStore,
            ChangeRef changeRef,
            Path path,
            OutputStream outputStream) throws IOException {
        
        Objects.requireNonNull(changeRef);
        Objects.requireNonNull(path);
        Objects.requireNonNull(outputStream);

        try (FileRetrieval changeInput = changeStore.createPrevStateChangeInput(changeRef)) {

            try (InputStream inputStream = changeInput.retrieveFile(path)) {

                IOUtils.applyAll(inputStream, outputStream);
            }
        }
        catch (IOException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new IOException("Failed to close", ex);
        }
    }
    
    private static MapOfList<Path, TextEdit>
        readEdits(FileRetrieval changeInput) throws IOException, XMLStreamException {

        final MapOfList<Path, TextEdit> edits;

        try (InputStream editsStream = changeInput.retrieveFile(ChangeSerializationPaths.EDITS_XML)) {

            if (editsStream != null) {
                try {
                    edits = EditsDeserializationHelper.deserializeEditFiles(editsStream);
                }
                finally {
                    editsStream.close();
                }
            }
            else {
                // No edits file
                edits = null;
            }
        }

        return edits;
    }
}
