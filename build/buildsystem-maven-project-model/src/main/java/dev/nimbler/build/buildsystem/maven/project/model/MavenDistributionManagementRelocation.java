package dev.nimbler.build.buildsystem.maven.project.model;

import dev.nimbler.build.buildsystem.maven.common.model.MavenEntity;
import dev.nimbler.build.buildsystem.maven.common.model.MavenModuleId;

public final class MavenDistributionManagementRelocation extends MavenEntity {

    private final String message;

    public MavenDistributionManagementRelocation(MavenModuleId moduleId, String message) {
        super(moduleId, null);

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
