package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import dev.nimbler.build.buildsystem.maven.project.model.MavenBuildPlugin;

interface PluginsSetter {

    void setPlugins(List<MavenBuildPlugin> plugins);
}
