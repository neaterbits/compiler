package dev.nimbler.build.buildsystem.maven.plugins;

import java.io.File;
import java.util.List;

import dev.nimbler.build.buildsystem.maven.model.MavenFileDependency;
import dev.nimbler.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;

public interface MavenPluginInfo {

    MavenPluginDescriptor getPluginDescriptor();

    File getPluginJarFile();

    List<MavenFileDependency> getAllDependencies();
}
