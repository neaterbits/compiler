package dev.nimbler.build.buildsystem.maven.project.model;

import java.util.Objects;

import dev.nimbler.build.buildsystem.maven.common.model.MavenModuleId;

public final class MavenParent {
    
    private final MavenModuleId moduleId;
    private final String relativePath;
    
    public MavenParent(MavenModuleId moduleId, String relativePath) {

        Objects.requireNonNull(moduleId);
        
        this.moduleId = moduleId;
        this.relativePath = relativePath;
    }

    public MavenModuleId getModuleId() {
        return moduleId;
    }

    public String getRelativePath() {
        return relativePath;
    }
}
