package dev.nimbler.build.buildsystem.maven.targets;

import dev.nimbler.build.buildsystem.maven.MavenBuildRoot;
import dev.nimbler.build.common.tasks.BuildSystemContext;

final class MavenBuilderContext extends BuildSystemContext<MavenBuildRoot> {

    MavenBuilderContext(MavenBuildRoot mavenBuildRoot) {
        super(mavenBuildRoot);
    }

    MavenBuilderContext(MavenBuilderContext context) {
        this(context.getBuildSystemRoot());
    }
}
