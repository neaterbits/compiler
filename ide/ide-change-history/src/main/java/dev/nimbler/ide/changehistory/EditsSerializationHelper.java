package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jutils.coll.MapOfList;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.FileStorage;
import dev.nimbler.ide.model.text.TextAdd;
import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.model.text.TextRemove;
import dev.nimbler.ide.model.text.TextReplace;

class EditsSerializationHelper {

    static void serializeEdits(
            SourceFileComplexChange complexChange,
            ChangeHistoryStorageFactory changeOutputFactory,
            Path ideBasePath) throws XMLStreamException, IOException {
    
        if (complexChange.getEdits() != null && !complexChange.getEdits().isEmpty()) {

            try (FileStorage editChangeOutput = changeOutputFactory.createEditsChangeOutput()) {
            
                try (OutputStream outputStream = editChangeOutput.addFile(ChangeSerializationPaths.EDITS_XML)) {
                
                    serializeFileEdits(complexChange.getEdits(), outputStream, ideBasePath);
                }
                catch (Exception ex) {
                    throw new IOException("Exception while closing edit change output stream", ex);
                }
            }
            catch (Exception ex) {
                throw new IOException("Exception while closing edit change output", ex);
            }
        }
    }

    static void serializeFileEdits(
            MapOfList<SourceFileResourcePath, TextEdit> editsMap,
            OutputStream outputStream,
            Path ideBasePath) throws XMLStreamException {
        
        final XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream);

        try {
            writer.writeStartDocument();

            serializeFileEdits(editsMap, writer, ideBasePath);
            
            writer.writeEndDocument();
        }
        finally {
            writer.close();
        }
    }
    
    private static void serializeFileEdits(
            MapOfList<SourceFileResourcePath, TextEdit> editsMap,
            XMLStreamWriter writer,
            Path ideBasePath) throws XMLStreamException {
        
        writer.writeStartElement("fileEdits");
        
        for (Map.Entry<SourceFileResourcePath, List<TextEdit>> edits : editsMap.entrySet()) {

            writer.writeStartElement("file");
            
            final String filePathString = ComplexChangeSerializationHelper.getRelativePathString(
                    edits.getKey(),
                    ideBasePath);

            writer.writeStartElement("path");
            writer.writeCharacters(filePathString);
            writer.writeEndElement();

            writer.writeStartElement("edits");
            for (TextEdit textEdit : edits.getValue()) {
                serializeEdit(textEdit, writer);
            }
            writer.writeEndElement(); // </edits>

            writer.writeEndElement(); // </file>
        }
        
        writer.writeEndElement(); // </files>
    }
    
    private static void serializeEdit(TextEdit textEdit, XMLStreamWriter writer) throws XMLStreamException {
        
        if (textEdit instanceof TextAdd) {
            writer.writeStartElement("add");

            serializePos(textEdit, writer);

            final TextAdd add = (TextAdd)textEdit;
            
            writer.writeStartElement("added");
            writer.writeCharacters(add.getAddedText().asString());
            writer.writeEndElement();

            writer.writeEndElement();
        }
        else if (textEdit instanceof TextReplace) {
            
            writer.writeStartElement("replace");

            serializePos(textEdit, writer);
 
            final TextReplace replace = (TextReplace)textEdit;
            
            writer.writeStartElement("replaced");
            writer.writeCharacters(replace.getOldText().asString());
            writer.writeEndElement();

            writer.writeStartElement("update");
            writer.writeCharacters(replace.getNewText().asString());
            writer.writeEndElement();

            writer.writeEndElement();
        }
        else if (textEdit instanceof TextRemove) {
            
            writer.writeStartElement("remove");

            serializePos(textEdit, writer);

            final TextRemove remove = (TextRemove)textEdit;
            
            writer.writeStartElement("removed");
            writer.writeCharacters(remove.getOldText().asString());
            writer.writeEndElement();
            
            writer.writeEndElement();
        }
        else {
            throw new IllegalStateException();
        }
    }
    
    private static void serializePos(TextEdit textEdit, XMLStreamWriter writer) throws XMLStreamException {
        
        writer.writeStartElement("pos");
        writer.writeCharacters(Long.toString(textEdit.getStartPos()));
        writer.writeEndElement();
    }
}
