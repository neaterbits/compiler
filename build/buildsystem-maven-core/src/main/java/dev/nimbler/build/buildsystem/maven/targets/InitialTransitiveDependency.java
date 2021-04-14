package dev.nimbler.build.buildsystem.maven.targets;

import java.util.List;

import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.project.model.BaseMavenRepository;
import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;

class InitialTransitiveDependency extends PomDependency {

    InitialTransitiveDependency(
            MavenProject originalDependency,
            List<BaseMavenRepository> referencedFromRepositories,
            MavenDependency transitiveDependency) {
        
        super(
                null,
                originalDependency,
                referencedFromRepositories,
                transitiveDependency);
    }
}
