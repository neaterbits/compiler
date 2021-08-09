package dev.nimbler.ide.changehistory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jutils.Value;

abstract class BaseDeserializationHelper {

    @FunctionalInterface
    interface ProcessTag {
        
        void process(String tagName) throws XMLStreamException;
    }
    
    static void verifyDocumentLevelTag(String documentLevelTag, XMLStreamReader xmlReader)
            throws XMLStreamException {

        if (xmlReader.nextTag() != XMLStreamReader.START_ELEMENT) {
            throw new XMLStreamException("Unexpected element type");
        }

        if (!xmlReader.getLocalName().equals(documentLevelTag)) {
            throw new XMLStreamException("Unexpected element '" + xmlReader.getLocalName() + "'");
        }
    }
    
    static void deserializeLevel(
            String endTag,
            XMLStreamReader xmlReader,
            ProcessTag processTag) throws XMLStreamException {
        
        for (;;) {
            
            final int next = xmlReader.nextTag();
            
            if (next == XMLStreamReader.START_ELEMENT) {

                final String tagName = xmlReader.getLocalName();
                
                processTag.process(tagName);
            }
            else if (next == XMLStreamReader.END_ELEMENT) {
                
                // if (!xmlReader.getLocalName().equals(endTag)) {
                //    throw new XMLStreamException("Unexpected element '" + xmlReader.getLocalName() + "' at end of '" + endTag + "'");
                // }
                
                break;
            }
            else {
                throw new XMLStreamException("Unexpected element type");
            }
        }
    }
    
    static XMLStreamException throwUnknownTag(String tagName) throws XMLStreamException {
        
        throw new XMLStreamException("Unknown tag '" + tagName + "'");
    }
    
    static void verifyElementValue(Value<?> element, String tagName) throws XMLStreamException {

        if (element.isNull()) {
            throw new XMLStreamException("Missing <" + tagName + "> element");
        }
    }
}
