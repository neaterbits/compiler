package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jutils.PathUtil;
import org.jutils.Value;
import org.jutils.coll.MapOfList;

import dev.nimbler.ide.model.text.TextAdd;
import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.model.text.TextRemove;
import dev.nimbler.ide.model.text.TextReplace;
import dev.nimbler.ide.util.ui.text.StringText;

class EditsDeserializationHelper extends BaseDeserializationHelper {

    static MapOfList<Path, TextEdit> deserializeEditFiles(InputStream inputStream)
            throws IOException, XMLStreamException {

        final MapOfList<Path, TextEdit> files;

        final XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);

        if (xmlReader.hasNext()) {

            verifyDocumentLevelTag("fileEdits", xmlReader);

            files = new MapOfList<>();

            // Temp storage
            final Value<String> value1 = new Value<>();
            final Value<String> value2 = new Value<>();
            final Value<String> value3 = new Value<>();
            final Value<String> value4 = new Value<>();

            final Value<List<TextEdit>> editsValue = new Value<>();

            deserializeLevel("fileEdits", xmlReader, tagName -> {

                switch (tagName) {
                case "file":
                    deserializeFile(xmlReader, files, value1, value2, value3, value4, editsValue);
                    break;

                default:
                    throw new XMLStreamException("Unknown file tag '" + tagName + "'");
                }
            });
        } else {
            files = null;
        }

        return files;
    }

    private static void deserializeFile(XMLStreamReader xmlReader, MapOfList<Path, TextEdit> files,
            Value<String> value1, Value<String> value2, Value<String> value3, Value<String> value4,
            Value<List<TextEdit>> editsValue) throws XMLStreamException {

        value1.setNull();
        editsValue.setNull();

        deserializeLevel("file", xmlReader, tagName -> {

            switch (tagName) {

            case "path":
                value1.set(xmlReader.getElementText());
                break;

            case "edits":
                final List<TextEdit> edits = deserializeEdits(xmlReader, value2, value3, value4);

                editsValue.set(edits);
                break;

            default:
                throw new XMLStreamException("Unknown file tag '" + tagName + "'");
            }
        });

        if (value1.isNull()) {
            throw new XMLStreamException("Missing <path> element for file");
        }

        if (editsValue.isNull()) {
            throw new XMLStreamException("Missing <edits> element for file");
        }

        final Path sourceFile = PathUtil.splitForwardSlashPathString(value1.get());

        files.addCollection(sourceFile, editsValue.get());
    }

    private static List<TextEdit> deserializeEdits(XMLStreamReader xmlReader, Value<String> value1,
            Value<String> value2, Value<String> value3) throws XMLStreamException {

        final List<TextEdit> edits = new ArrayList<>();

        deserializeLevel("edits", xmlReader, tagName -> {

            final TextEdit textEdit;

            switch (tagName) {
            case "add":
                textEdit = deserializeAdd(xmlReader, value1, value2);
                break;

            case "replace":
                textEdit = deserializeReplace(xmlReader, value1, value2, value3);
                break;

            case "remove":
                textEdit = deserializeRemove(xmlReader, value1, value2);
                break;

            default:
                throw throwUnknownTag(tagName);
            }

            edits.add(textEdit);
        });

        return edits;
    }

    private static TextAdd deserializeAdd(XMLStreamReader xmlReader, Value<String> value1, Value<String> value2)
            throws XMLStreamException {

        value1.setNull();
        value2.setNull();

        deserializeLevel("add", xmlReader, tagName -> {

            switch (tagName) {
            case "pos":
                value1.set(xmlReader.getElementText());
                break;

            case "added":
                value2.set(xmlReader.getElementText());
                break;

            default:
                throwUnknownTag(tagName);
            }
        });

        verifyElementValue(value1, "pos");
        verifyElementValue(value2, "added");

        return new TextAdd(
                Long.parseLong(value1.get()),
                new StringText(value2.get()));
    }

    private static TextReplace deserializeReplace(XMLStreamReader xmlReader, Value<String> value1, Value<String> value2,
            Value<String> value3) throws XMLStreamException {

        value1.setNull();
        value2.setNull();
        value3.setNull();

        deserializeLevel("replace", xmlReader, tagName -> {

            switch (tagName) {
            case "pos":
                value1.set(xmlReader.getElementText());
                break;

            case "replaced":
                value2.set(xmlReader.getElementText());
                break;

            case "update":
                value3.set(xmlReader.getElementText());
                break;

            default:
                throw new XMLStreamException("Unknown tag '" + tagName + "'");
            }
        });
        
        verifyElementValue(value1, "pos");
        verifyElementValue(value2, "replaced");
        verifyElementValue(value3, "update");

        final String replaced = value2.get();

        return new TextReplace(
                Long.parseLong(value1.get()),
                replaced.length(),
                new StringText(replaced),
                new StringText(value3.get()));
    }

    private static TextRemove deserializeRemove(XMLStreamReader xmlReader, Value<String> value1, Value<String> value2)
            throws XMLStreamException {

        value1.setNull();
        value2.setNull();

        deserializeLevel("add", xmlReader, tagName -> {

            switch (tagName) {
            case "pos":
                value1.set(xmlReader.getElementText());
                break;

            case "removed":
                value2.set(xmlReader.getElementText());
                break;

            default:
                throwUnknownTag(tagName);
            }
        });
        
        verifyElementValue(value1, "pos");

        verifyElementValue(value2, "added");

        final String removed = value2.get();

        return new TextRemove(
                Long.parseLong(value1.get()),
                removed.length(),
                new StringText(removed));
    }
}
