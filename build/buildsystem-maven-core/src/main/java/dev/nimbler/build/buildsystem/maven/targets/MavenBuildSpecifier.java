package dev.nimbler.build.buildsystem.maven.targets;

import java.util.List;

import dev.nimbler.build.buildsystem.common.ArgumentException;
import dev.nimbler.build.buildsystem.common.BuildSpecifier;
import dev.nimbler.build.buildsystem.common.BuildSystemRootScan;
import dev.nimbler.build.buildsystem.maven.MavenBuildRoot;
import dev.nimbler.build.buildsystem.maven.phases.UnknownPhaseException;
import dev.nimbler.build.types.Build;

public final class MavenBuildSpecifier implements BuildSpecifier<MavenBuilderContext> {

    @Override
    public List<Build<MavenBuilderContext>> specifyBuild(String[] args) throws ArgumentException {

        try {
            return MavenPhasesAndGoalsToTargets.makeTargetBuilders(args);
        } catch (UnknownPhaseException | UnknownGoalOrPhaseException ex) {
            throw new ArgumentException(ex);
        }
    }

    @Override
    public MavenBuilderContext createTaskContext(BuildSystemRootScan buildSystemRoot) {
        
        final MavenBuildRoot mavenBuildRoot = (MavenBuildRoot)buildSystemRoot;
        
        return new MavenBuilderContext(mavenBuildRoot);
    }
}
