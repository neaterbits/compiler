package dev.nimbler.build.types;

import java.util.Objects;

import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.scheduling.task.TaskContext;

public final class Build<CTX extends TaskContext> {
    private final TargetBuilderSpec<CTX> targetBuilder;
    private final String targetName;
    
    public Build(TargetBuilderSpec<CTX> targetBuilder, String targetName) {

        Objects.requireNonNull(targetBuilder);
        Objects.requireNonNull(targetName);
        
        this.targetBuilder = targetBuilder;
        this.targetName = targetName;
    }

    public TargetBuilderSpec<CTX> getTargetBuilder() {
        return targetBuilder;
    }

    public String getTargetName() {
        return targetName;
    }
}