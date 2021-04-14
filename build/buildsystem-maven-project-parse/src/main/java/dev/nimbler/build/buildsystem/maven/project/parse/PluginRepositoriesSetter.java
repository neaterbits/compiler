package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import dev.nimbler.build.buildsystem.maven.project.model.MavenPluginRepository;

interface PluginRepositoriesSetter {

    void setPluginRepositories(List<MavenPluginRepository> pluginRepositories);
}
