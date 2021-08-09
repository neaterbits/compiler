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

abstract class BaseChangeHistory implements ChangeHistory, AutoCloseable {

    private final Path ideBasePath;
    private final ChangeStore changeStore;
    private final boolean serializePrevState;

    BaseChangeHistory(Path ideBasePath, ChangeStore changeStore, boolean serializePrevState) {

        Objects.requireNonNull(ideBasePath);
        Objects.requireNonNull(changeStore);
        
        this.ideBasePath = ideBasePath;
        this.changeStore = changeStore;
        this.serializePrevState = serializePrevState;
    }

    @Override
    public final void addTextEdit(SourceFileResourcePath sourceFile, TextEdit textEdit) throws IOException {

        final MapOfList<SourceFileResourcePath, TextEdit> mapOfList = new MapOfList<>(1);
        
        mapOfList.add(sourceFile, textEdit);
        
        addRefactorChange(null, null, mapOfList);
    }

    @Override
    public final void addRefactorChange(
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
    public final void addHistoryMoveChange(
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
    public final void iterateChanges(boolean chronological, ChangeHistoryIterator iterator) throws IOException {

        changeStore.iterateChanges(chronological, iterator::onChange);
    }

    @Override
    public final HistoricChange getHistoricChange(ReasonChangeRef changeRef) throws IOException {

        return ComplexChangeDeserializationHelper.deserialize(changeStore, changeRef);
    }

    @Override
    public final void deserializePrevStateFile(
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

        Objects.requireNonNull(change);

        try (ChangeHistoryStorageFactory changeOutputFactory = changeStore.prepare(change)) {
            
            ComplexChangeSerializationHelper.serialize(
                    change,
                    changeOutputFactory,
                    ideBasePath,
                    serializePrevState);
        }
        catch (IOException | XMLStreamException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new IOException("Exception while closing change output factory", ex);
        }
    }
}
