package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.FileStorage;

class RefactorSerializationHelper {
    
    private static final String REFACTOR_ROOT_TAG = "refactor";

    private static final String HISTORY_MOVE_ROOT_TAG = "historyMove";

    static void serializeRefactor(
            SourceFileComplexChange complexChange,
            ChangeHistoryStorageFactory changeOutputFactory,
            Path ideBasePath) throws IOException {

        if (complexChange.getChangeReason() != ChangeReason.REFACTOR) {
            throw new IllegalStateException();
        }
        
        serializeMoveOrRefactor(
                REFACTOR_ROOT_TAG,
                ChangeSerializationPaths.REFACTOR_XML,
                complexChange,
                changeOutputFactory,
                ideBasePath);
    }

    static void serializeRefactor(
            SourceFileComplexChange complexChange,
            OutputStream outputStream,
            Path ideBasePath) throws XMLStreamException {
        
        serializeMoveOrRefactor(REFACTOR_ROOT_TAG, complexChange, outputStream, ideBasePath);
    }

    static void serializeHistoryMove(
            SourceFileComplexChange complexChange,
            ChangeHistoryStorageFactory changeOutputFactory,
            Path ideBasePath) throws IOException {

        if (!complexChange.getChangeReason().isHistoryMove()) {
            throw new IllegalStateException();
        }
        
        serializeMoveOrRefactor(
                HISTORY_MOVE_ROOT_TAG,
                ChangeSerializationPaths.HISTORY_MOVE_XML,
                complexChange,
                changeOutputFactory,
                ideBasePath);
    }

    static void serializeHistoryMove(
            SourceFileComplexChange complexChange,
            OutputStream outputStream,
            Path ideBasePath) throws XMLStreamException {
        
        serializeMoveOrRefactor(HISTORY_MOVE_ROOT_TAG, complexChange, outputStream, ideBasePath);
    }
    
    private static void serializeMoveOrRefactor(
            String rootTag,
            Path xmlFileName,
            SourceFileComplexChange complexChange,
            ChangeHistoryStorageFactory changeOutputFactory,
            Path ideBasePath) throws IOException {

        try (FileStorage editChangeOutput = changeOutputFactory.createEditsChangeOutput()) {
            
            try (OutputStream outputStream = editChangeOutput.addFile(xmlFileName)) {

                serializeMoveOrRefactor(rootTag, complexChange, outputStream, ideBasePath);
            }
            catch (Exception ex) {
                throw new IOException("Exception while closing refactor change output stream", ex);
            }
        }
        catch (Exception ex) {
            throw new IOException("Exception while closing refactor change output", ex);
        }
    }

    private static void serializeMoveOrRefactor(
            String rootTag,
            SourceFileComplexChange complexChange,
            OutputStream outputStream,
            Path ideBasePath) throws XMLStreamException {

        final XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream);

        try {
            serializeMoveOrRefactor(rootTag, complexChange, writer, ideBasePath);
        }
        finally {
            writer.close();
        }
    }

    private static void serializeMoveOrRefactor(
            String rootTag,
            SourceFileComplexChange complexChange,
            XMLStreamWriter writer,
            Path ideBasePath) throws XMLStreamException {
        
        writer.writeStartDocument();
        
        writer.writeStartElement(rootTag);
        
        serializeMoveOrRefactorContents(complexChange, writer, ideBasePath);
        
        writer.writeEndElement();
        
        writer.writeEndDocument();
    }
    
    private static void serializeMoveOrRefactorContents(
            SourceFileComplexChange complexChange,
            XMLStreamWriter writer,
            Path ideBasePath) throws XMLStreamException {

        if (complexChange.getAddFiles() != null && !complexChange.getAddFiles().isEmpty()) {

            serializeAdds(writer, complexChange.getAddFiles(), ideBasePath);
        }

        if (complexChange.getRemoveFiles() != null && !complexChange.getRemoveFiles().isEmpty()) {

            serializeRemoves(writer, complexChange.getRemoveFiles(), ideBasePath);
        }

        if (complexChange.getMoveFiles() != null && !complexChange.getMoveFiles().isEmpty()) {

            serializeMoves(writer, complexChange.getMoveFiles(), ideBasePath);
        }
    }

    private static void serializeAdds(
            XMLStreamWriter writer,
            List<SourceFileResourcePath> adds,
            Path ideBasePath) throws XMLStreamException {

        serializeList("adds", "add", writer, adds, ideBasePath);
    }

    private static void serializeRemoves(
            XMLStreamWriter writer,
            List<SourceFileResourcePath> removes,
            Path ideBasePath) throws XMLStreamException {

        serializeList("removes", "remove", writer, removes, ideBasePath);
    }

    private static void serializeList(
            String listTag,
            String elementTag,
            XMLStreamWriter writer,
            List<SourceFileResourcePath> removes,
            Path ideBasePath) throws XMLStreamException {

        writer.writeStartElement(listTag);

        for (SourceFileResourcePath removePath : removes) {

            writer.writeStartElement(elementTag);

            writer.writeCharacters(
                    ComplexChangeSerializationHelper.getRelativePathString(
                            removePath,
                            ideBasePath));

            writer.writeEndElement();
        }

        writer.writeEndElement();
    }

    private static void serializeMoves(
            XMLStreamWriter writer,
            Map<SourceFileResourcePath, SourceFileResourcePath> moves,
            Path ideBasePath) throws XMLStreamException {
        
        writer.writeStartElement("moves");

        for (Map.Entry<SourceFileResourcePath, SourceFileResourcePath> entry : moves.entrySet()) {

            writer.writeStartElement("move");

            writer.writeStartElement("from");
            writer.writeCharacters(ComplexChangeSerializationHelper.getRelativePathString(entry.getKey(), ideBasePath));
            writer.writeEndElement();

            writer.writeStartElement("to");
            writer.writeCharacters(ComplexChangeSerializationHelper.getRelativePathString(entry.getValue(), ideBasePath));
            writer.writeEndElement();

            writer.writeEndElement();
        }

        writer.writeEndElement();
    }
}
