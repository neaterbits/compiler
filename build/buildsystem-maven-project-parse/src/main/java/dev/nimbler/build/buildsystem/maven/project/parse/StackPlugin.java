package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;
import dev.nimbler.build.buildsystem.maven.common.parse.DependenciesSetter;
import dev.nimbler.build.buildsystem.maven.common.parse.StackEntity;
import dev.nimbler.build.buildsystem.maven.common.parse.configuration.ConfigurationSetter;
import dev.nimbler.build.buildsystem.maven.project.model.MavenExecution;

final class StackPlugin
        extends StackEntity
        implements DependenciesSetter, InheritedSetter, ConfigurationSetter {

    private Boolean extensions;

    private Boolean inherited;

    private PlexusConfigurationMap configuration;

    private List<MavenDependency> dependencies;

    private List<MavenExecution> executions;

	StackPlugin(Context context) {
		super(context);
	}

    Boolean getExtensions() {
        return extensions;
    }

    void setExtensions(Boolean extensions) {
        this.extensions = extensions;
    }

    Boolean getInherited() {
        return inherited;
    }

    @Override
    public void setInherited(Boolean inherited) {
        this.inherited = inherited;
    }

    PlexusConfigurationMap getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(PlexusConfigurationMap configuration) {
        this.configuration = configuration;
    }

    List<MavenDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {
        this.dependencies = dependencies;
    }

    List<MavenExecution> getExecutions() {
        return executions;
    }

    void setExecutions(List<MavenExecution> executions) {
        this.executions = executions;
    }
}
