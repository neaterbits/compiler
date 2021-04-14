package dev.nimbler.build.buildsystem.maven;

import java.util.List;
import java.util.Objects;

import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.common.model.MavenModuleId;
import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;

public final class EffectiveProject {

    private final MavenProject project;

    EffectiveProject(MavenProject project) {

        Objects.requireNonNull(project);
        
        this.project = project;
    }

    public MavenModuleId getModuleId() {
        return project.getModuleId();
    }

    public List<MavenDependency> getDependencies() {
        return project.getCommon().getDependencies();
    }
}
