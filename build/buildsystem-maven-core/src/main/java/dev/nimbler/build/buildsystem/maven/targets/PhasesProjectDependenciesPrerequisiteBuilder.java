package dev.nimbler.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;

import dev.nimbler.build.buildsystem.maven.MavenBuildRoot;
import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.phases.Phase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;
import dev.nimbler.build.common.tasks.ModuleBuilderUtil;

final class PhasesProjectDependenciesPrerequisiteBuilder extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private final Phase phase;
    
    PhasesProjectDependenciesPrerequisiteBuilder(Phase phase) {

        Objects.requireNonNull(phase);

        this.phase = phase;
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder
            .withPrerequisites("Project dependencies")
            .fromIterating(
                    null,
                    (context, target) -> transitiveProjectDependencies(context.getBuildSystemRoot(), target.getProject()).stream()
                .map(dep -> new PhaseMavenProject(phase, dep))
                .collect(Collectors.toList()));
    }

    private List<MavenProject> transitiveProjectDependencies(MavenBuildRoot buildRoot, MavenProject project) {
        
        final List<MavenDependency> deps = ModuleBuilderUtil.transitiveProjectDependencies(
                project,
                p -> buildRoot.getDirectDependenciesOfProject(p),
                dependency -> buildRoot.getProject(dependency));

        return deps.stream()
                .map(buildRoot::getProject)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}


