package dev.nimbler.build.buildsystem.maven.project.parse;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;

import com.neaterbits.util.IOUtils;

import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;
import dev.nimbler.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderException;
import dev.nimbler.build.buildsystem.maven.xml.dom.DOMReaderFactory;

public abstract class BasePomParserTest {

    final MavenProject parse(String pomString) throws IOException, XMLReaderException {

        final MavenXMLProject<Document> pom;

        final File file = File.createTempFile("pom", "xml");

        final DOMReaderFactory xmlReaderFactory = new DOMReaderFactory();

        try {
            file.deleteOnExit();
        
            IOUtils.write(file, pomString);
            
            pom = PomTreeParser.readModule(file, xmlReaderFactory);
        }
        finally {
            file.delete();
        }

        final MavenProject project = pom.getProject();

        return project;
    }
}
