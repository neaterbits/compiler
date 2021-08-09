package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jutils.PathUtil;
import org.jutils.Value;
import org.jutils.coll.MapOfList;

import dev.nimbler.ide.model.text.TextEdit;

class RefactorDeserializationHelper extends BaseDeserializationHelper {

    static PathComplexChange deserializeRefactorFile(
                InputStream inputStream,
                ChangeReason change,
                MapOfList<Path, TextEdit> edits)
    
            throws IOException, XMLStreamException {
        
        return deserializeRefactorOrHistoryMoveFile("refactor", inputStream, change, edits);
    }

    static PathComplexChange deserializeHistoryMoveFile(
            InputStream inputStream,
            ChangeReason change,
            MapOfList<Path, TextEdit> edits)

        throws IOException, XMLStreamException {

        return deserializeRefactorOrHistoryMoveFile("historyMove", inputStream, change, edits);
    }

    private static PathComplexChange deserializeRefactorOrHistoryMoveFile(
            String rootTag,
            InputStream inputStream,
            ChangeReason change,
            MapOfList<Path, TextEdit> edits) throws XMLStreamException {

        final PathComplexChange complexChange;
        
        final XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);

        try {
            if (xmlReader.hasNext()) {

                verifyDocumentLevelTag(rootTag, xmlReader);
                
                complexChange = deserializeRefactorOrMoveContents(rootTag, xmlReader, change, edits);
    
            } else {
                complexChange = new PathComplexChange(change, null, null, null, edits);
            }
        }
        finally {
            xmlReader.close();
        }

        return complexChange;
    }
    
    private static PathComplexChange deserializeRefactorOrMoveContents(
            String rootTag,
            XMLStreamReader xmlReader,
            ChangeReason change,
            MapOfList<Path, TextEdit> edits) throws XMLStreamException {
        
        // Temp storage
        final Value<List<Path>> addsValue = new Value<>();
        final Value<List<Path>> removesValue = new Value<>();
        final Value<Map<Path, Path>> movesValue = new Value<>();
        
        deserializeLevel(rootTag, xmlReader, tagName -> {
            
            switch (tagName) {
            case "adds":
                final List<Path> adds = deserializeAdds(xmlReader);

                addsValue.set(adds);
                break;

            case "removes":
                final List<Path> removes = deserializeRemoves(xmlReader);

                removesValue.set(removes);
                break;
                
            case "moves":
                final Map<Path, Path> moves = deserializeMoves(xmlReader);
                
                movesValue.set(moves);
                break;
                
            default:
                throwUnknownTag(tagName);
            }
        });
        
        final PathComplexChange complexChange = new PathComplexChange(
                    change,
                    addsValue.get(),
                    removesValue.get(),
                    movesValue.get(),
                    edits);
        
        return complexChange;
    }

    private static List<Path> deserializeAdds(XMLStreamReader xmlReader) throws XMLStreamException {
        
        final List<Path> adds = new ArrayList<>();
        
        deserializeLevel("adds", xmlReader, tagName -> {
            
            switch (tagName) {
            case "add":
                final String pathString = xmlReader.getElementText();
                final Path removePath = PathUtil.splitForwardSlashPathString(pathString);

                adds.add(removePath);
                break;
                
            default:
                throwUnknownTag(tagName);
            }
        });

        return adds;
    }
    
    private static List<Path> deserializeRemoves(XMLStreamReader xmlReader) throws XMLStreamException {
        
        final List<Path> removes = new ArrayList<>();
        
        deserializeLevel("removes", xmlReader, tagName -> {
            
            switch (tagName) {
            case "remove":
                final String pathString = xmlReader.getElementText();
                final Path removePath = PathUtil.splitForwardSlashPathString(pathString);

                removes.add(removePath);
                break;
                
            default:
                throwUnknownTag(tagName);
            }
        });

        return removes;
    }

    private static Map<Path, Path> deserializeMoves(XMLStreamReader xmlReader) throws XMLStreamException {
        
        final Map<Path, Path> moves = new HashMap<>();

        final Value<String> value1 = new Value<>();
        final Value<String> value2 = new Value<>();

        deserializeLevel("moves", xmlReader, tagName -> {
            
            switch (tagName) {
            case "move":
                deserializeMove(xmlReader, moves, value1, value2);
                break;
                
            default:
                throwUnknownTag(tagName);
            }
        });

        return moves;
    }

    private static void deserializeMove(
            XMLStreamReader xmlReader,
            Map<Path, Path> moves,
            Value<String> value1,
            Value<String> value2) throws XMLStreamException {
        
        value1.setNull();
        value2.setNull();
        
        deserializeLevel("move", xmlReader, tagName -> {
            
            switch (tagName) {
            case "from":
                value1.set(xmlReader.getElementText());
                break;
                
            case "to":
                value2.set(xmlReader.getElementText());
                break;

            default:
                throwUnknownTag(tagName);
            }
        });

        verifyElementValue(value1, "from");
        verifyElementValue(value2, "to");
    
        final Path from = PathUtil.splitForwardSlashPathString(value1.get());
        final Path to   = PathUtil.splitForwardSlashPathString(value2.get());
        
        moves.put(from, to);
    }
}
