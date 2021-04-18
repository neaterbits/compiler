package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.common.parse.DependenciesSetter;

final class StackDependencyManagement extends StackBase implements DependenciesSetter {

    private List<MavenDependency> dependencies;

    StackDependencyManagement(Context context) {
        super(context);
    }

    List<MavenDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {
        this.dependencies = dependencies;
    }
}
