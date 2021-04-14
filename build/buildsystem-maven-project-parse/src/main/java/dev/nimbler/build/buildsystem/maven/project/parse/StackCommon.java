package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.project.model.MavenBuild;
import dev.nimbler.build.buildsystem.maven.project.model.MavenCommon;
import dev.nimbler.build.buildsystem.maven.project.model.MavenDependencyManagement;
import dev.nimbler.build.buildsystem.maven.project.model.MavenDistributionManagement;
import dev.nimbler.build.buildsystem.maven.project.model.MavenPluginRepository;
import dev.nimbler.build.buildsystem.maven.project.model.MavenReporting;
import dev.nimbler.build.buildsystem.maven.project.model.MavenRepository;

final class StackCommon {

    private List<String> modules;
    private MavenBuild build;
    private MavenReporting reporting;
    private List<MavenRepository> repositories;
    private List<MavenPluginRepository> pluginRepositories;
    private MavenDependencyManagement dependencyManagement;
    private List<MavenDependency> dependencies;
    private MavenDistributionManagement distributionManagement;

    MavenCommon makeMavenCommon() {
        
        return new MavenCommon(
                modules,
                build,
                reporting,
                repositories,
                pluginRepositories,
                dependencyManagement,
                distributionManagement,
                dependencies);
    }
    
    void setModules(List<String> modules) {
        this.modules = modules;
    }

    void setDependencyManagement(MavenDependencyManagement dependencyManagement) {
        this.dependencyManagement = dependencyManagement;
    }

    void setDistributionManagement(MavenDistributionManagement distributionManagement) {
        this.distributionManagement = distributionManagement;
    }

    void setDependencies(List<MavenDependency> dependencies) {
        this.dependencies = dependencies;
    }

    void setBuild(MavenBuild build) {
        this.build = build;
    }

    void setReporting(MavenReporting reporting) {
        this.reporting = reporting;
    }

    void setRepositories(List<MavenRepository> repositories) {
        this.repositories = repositories;
    }

    void setPluginRepositories(List<MavenPluginRepository> pluginRepositories) {
        this.pluginRepositories = pluginRepositories;
    }
}
