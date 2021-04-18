package dev.nimbler.build.strategies.common;

import java.util.Collection;
import java.util.Objects;

import org.jutils.concurrency.scheduling.task.TaskContext;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;

abstract class BuildRootContext extends TaskContext {

    private final BuildRoot buildRoot;

    BuildRootContext(BuildRoot buildRoot) {

        Objects.requireNonNull(buildRoot);

        this.buildRoot = buildRoot;
    }
    
    BuildRootContext(BuildRootContext context) {
        this(context.buildRoot);
    }

    public final BuildRoot getBuildRoot() {
        return buildRoot;
    }

    public final Collection<ProjectModuleResourcePath> getModules() {
        return buildRoot.getModules();
    }
    
    public final TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath path) {

        return buildRoot.getTargetDirectory(path);
    }

    public final CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module) {
        return buildRoot.getCompiledModuleFile(module);
    }
}
