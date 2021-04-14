package dev.nimbler.build.buildsystem.maven.plugins.descriptor.parse;

import java.io.IOException;
import java.io.InputStream;

import dev.nimbler.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import dev.nimbler.build.buildsystem.maven.xml.XMLReader;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderException;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderFactory;

public class MavenPluginDescriptorParser {

    public static <DOCUMENT> MavenPluginDescriptor read(
            InputStream inputStream,
            XMLReaderFactory<DOCUMENT> xmlReaderFactory,
            String filePath) throws XMLReaderException, IOException {
    
        final StackPluginDescriptorEventListener eventListener = new StackPluginDescriptorEventListener();
    
        
        final XMLReader<DOCUMENT> xmlReader = xmlReaderFactory.createReader(inputStream, filePath);
        
        xmlReader.readXML(
                new MavenPluginDescriptorXMLEventListener(eventListener),
                null);

        return eventListener.getPluginDescriptor();
    }
}
