package dev.nimbler.build.buildsystem.maven.projects;

import java.io.File;

import dev.nimbler.build.buildsystem.common.ScanException;
import dev.nimbler.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderFactory;

public interface MavenProjectsAccess {

    <DOCUMENT> MavenXMLProject<DOCUMENT>
    readModule(File pomFile, XMLReaderFactory<DOCUMENT> readerFactory) throws ScanException;

}
