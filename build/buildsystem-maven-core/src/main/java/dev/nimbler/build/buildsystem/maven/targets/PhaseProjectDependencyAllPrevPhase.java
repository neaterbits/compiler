package dev.nimbler.build.buildsystem.maven.targets;

import java.util.Objects;

import org.jutils.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import org.jutils.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;

import dev.nimbler.build.buildsystem.maven.phases.Phase;

final class PhaseProjectDependencyAllPrevPhase extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private final Phase prevPhase;

    PhaseProjectDependencyAllPrevPhase(Phase prevPhase) {

        Objects.requireNonNull(prevPhase);
        
        this.prevPhase = prevPhase;
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder.withPrerequisite("Complete previous phase")
            .from(target -> prevPhase.getName());
    }
}
