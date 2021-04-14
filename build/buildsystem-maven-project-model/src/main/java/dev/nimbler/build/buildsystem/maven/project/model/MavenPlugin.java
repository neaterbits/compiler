package dev.nimbler.build.buildsystem.maven.project.model;

import dev.nimbler.build.buildsystem.maven.common.model.MavenEntity;
import dev.nimbler.build.buildsystem.maven.common.model.MavenModuleId;

public class MavenPlugin extends MavenEntity {

    public MavenPlugin(String groupId, String artifactId, String version) {
        
        this(new MavenModuleId(groupId, artifactId, version));
    }

    public MavenPlugin(MavenModuleId moduleId) {
        super(moduleId, null);
    }
}
