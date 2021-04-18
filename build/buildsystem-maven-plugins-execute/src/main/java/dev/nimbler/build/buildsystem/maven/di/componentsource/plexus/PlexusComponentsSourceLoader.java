package dev.nimbler.build.buildsystem.maven.di.componentsource.plexus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.jutils.di.componentsource.BaseComponentsSourceLoader;
import org.jutils.di.componentsource.ComponentsSource;
import org.jutils.di.componentsource.ComponentsSourceException;

import dev.nimbler.build.buildsystem.maven.plexus.components.model.PlexusComponent;
import dev.nimbler.build.buildsystem.maven.plexus.components.model.PlexusComponentSet;
import dev.nimbler.build.buildsystem.maven.plexus.components.parse.PlexusComponentDescriptorParser;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderException;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderFactory;
import dev.nimbler.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

public class PlexusComponentsSourceLoader
        extends BaseComponentsSourceLoader<
                PlexusComponent,
                PlexusComponentSet,
                PlexusDescriptorComponentsSource> {

    private static final XMLReaderFactory<?> XML_READER_FACTORY = new JavaxXMLStreamReaderFactory();
    
    @Override
    public List<? extends ComponentsSource<PlexusComponent>>
    load(URLClassLoader classLoader) throws IOException, ComponentsSourceException {

        final List<PlexusDescriptorComponentsSource> sources = processAll(
                classLoader,
                "META-INF/plexus/components.xml",
                PlexusComponentsSourceLoader::readComponentSet,
                PlexusDescriptorComponentsSource::new);
        
        return sources;
    }

    private static PlexusComponentSet readComponentSet(URL url, InputStream inputStream) throws IOException, ComponentsSourceException {
                
        final PlexusComponentSet componentSet;

        try {
            componentSet = PlexusComponentDescriptorParser.readDescriptor(
                                                    inputStream,
                                                    XML_READER_FACTORY,
                                                    url.getFile());
        } catch (XMLReaderException ex) {
            throw new ComponentsSourceException("Exception while parsing XML for " + url, ex);
        }

        return componentSet;
    }
}
