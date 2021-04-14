package dev.nimbler.build.buildsystem.maven.project.parse;

import dev.nimbler.build.buildsystem.maven.project.model.MavenDistributionManagement;

interface DistributionManagementSetter {

    void setDistributionManagement(MavenDistributionManagement distributionManagement);
}
