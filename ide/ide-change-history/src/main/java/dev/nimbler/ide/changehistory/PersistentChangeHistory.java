package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.stream.XMLStreamException;

import org.jutils.coll.MapOfList;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.model.text.TextEdit;

/**
 * Implementation of {@link ChangeHistory} that persists to file system.
 */

public final class PersistentChangeHistory implements ChangeHistory, AutoCloseable {

    private final Path ideBasePath;
    private final ChangeStore changeStore;
    
    /**
     * Construct a new {@link PersistentChangeHistory}
     * 
     * @param changeHistoryStorageBasePath base filesystem path to store the edit history
     * @param ideBasePath the base path to the IDE projects,
     *        for computing relative path to source files
     * 
     * @throws IOException if failed to initialize
     */

    public PersistentChangeHistory(Path changeHistoryStorageBasePath, Path ideBasePath)
                throws IOException {

        Objects.requireNonNull(changeHistoryStorageBasePath);
        Objects.requireNonNull(ideBasePath);
        
        this.ideBasePath = ideBasePath;
        
        this.changeStore = new UUIDChangeStore(changeHistoryStorageBasePath, 1000);
    }

    @Override
    public void addTextEdit(SourceFileResourcePath sourceFile, TextEdit textEdit) throws IOException {

        final MapOfList<SourceFileResourcePath, TextEdit> mapOfList = new MapOfList<>(1);
        
        mapOfList.add(sourceFile, textEdit);
        
        addRefactorChange(null, null, mapOfList);
    }

    @Override
    public void addRefactorChange(
            List<SourceFileResourcePath> removeFiles,
            Map<SourceFileResourcePath, SourceFileResourcePath> moveFiles,
            MapOfList<SourceFileResourcePath, TextEdit> edits) throws IOException {

        final SourceFileComplexChange change = new SourceFileComplexChange(
                                            ChangeReason.REFACTOR,
                                            null,
                                            null,
                                            removeFiles,
                                            moveFiles,
                                            edits);
        try {
            writeChange(change);
        } catch (XMLStreamException ex) {
            throw new IOException("Could not serialize", ex);
        }
    }
    
    @Override
    public void addHistoryMoveChange(
            ChangeReason changeReason,
            ChangeRef movedToRef,
            List<SourceFileResourcePath> addFiles,
            List<SourceFileResourcePath> removeFiles,
            Map<SourceFileResourcePath, SourceFileResourcePath> moveFiles,
            MapOfList<SourceFileResourcePath, TextEdit> edits) throws IOException {

        if (!changeReason.isHistoryMove()) {
            throw new IllegalArgumentException();
        }
        
        Objects.requireNonNull(movedToRef);
    
        final SourceFileComplexChange change = new SourceFileComplexChange(
                changeReason,
                movedToRef,
                addFiles,
                removeFiles,
                moveFiles,
                edits);
        try {
            writeChange(change);
        } catch (XMLStreamException ex) {
            throw new IOException("Could not serialize", ex);
        }
    }

    @Override
    public void iterateChanges(boolean chronological, ChangeHistoryIterator iterator) throws IOException {

        changeStore.iterateChanges(chronological, iterator::onChange);
    }

    @Override
    public HistoricChange getHistoricChange(ReasonChangeRef changeRef) throws IOException {

        return ComplexChangeDeserializationHelper.deserialize(changeStore, changeRef);
    }

    @Override
    public void deserializePrevStateFile(
            ChangeRef changeRef,
            Path path,
            OutputStream outputStream) throws IOException {

        ComplexChangeDeserializationHelper.deserializePrevStateFile(
                changeStore,
                changeRef,
                path,
                outputStream);
    }

    @Override
    public void close() throws Exception {

        changeStore.close();
    }

    private void writeChange(SourceFileComplexChange change) throws IOException, XMLStreamException {
        
        try (ChangeHistoryStorageFactory changeOutputFactory = changeStore.prepare(change)) {
            
            ComplexChangeSerializationHelper.serialize(
                    change,
                    changeOutputFactory,
                    ideBasePath);
        }
        catch (IOException | XMLStreamException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new IOException("Exception while closing change output factory", ex);
        }
    }
}
