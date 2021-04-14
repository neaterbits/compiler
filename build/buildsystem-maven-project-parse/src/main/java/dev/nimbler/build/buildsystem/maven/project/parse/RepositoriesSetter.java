package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import dev.nimbler.build.buildsystem.maven.project.model.MavenRepository;

interface RepositoriesSetter {

    void setRepositories(List<MavenRepository> repositories);
}
