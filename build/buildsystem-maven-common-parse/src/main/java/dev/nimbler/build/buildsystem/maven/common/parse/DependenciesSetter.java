package dev.nimbler.build.buildsystem.maven.common.parse;

import java.util.List;

import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;

public interface DependenciesSetter {

    void setDependencies(List<MavenDependency> dependencies);
}
