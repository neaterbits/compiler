package dev.nimbler.build.buildsystem.maven.targets;

import java.util.Objects;

import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;

import dev.nimbler.build.buildsystem.maven.phases.Phase;

final class PhaseProjectDependencySameModulePrevPhase extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private final Phase prevPhase;

    PhaseProjectDependencySameModulePrevPhase(Phase prevPhase) {
    
        Objects.requireNonNull(prevPhase);
        
        this.prevPhase = prevPhase;
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        if (prevPhase != null) {
            
            builder.withPrerequisite("Module prev phase '" + prevPhase.getName())
                .from(target -> { 
                    return PhaseMavenProject.from(prevPhase, target.getProject());
                });
        }
    }
}
