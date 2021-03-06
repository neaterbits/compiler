package dev.nimbler.build.buildsystem.maven.targets;

import java.util.List;

import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.project.model.BaseMavenRepository;
import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;

class PomDependency extends TransitiveDependency {

    private final MavenDependency dependency;
    
    PomDependency(
            ProjectDependency referencedFrom,
            MavenProject projectReferencedFrom,
            List<BaseMavenRepository> referencedFromRepositories,
            MavenDependency transitiveDependency) {
        
        super(
                referencedFrom,
                projectReferencedFrom,
                referencedFromRepositories,
                transitiveDependency.getModuleId());
        
        this.dependency = transitiveDependency;
    }

    @Override
    final String getTargetedDependencyClassifier() {
        return dependency.getClassifier();
    }
}
