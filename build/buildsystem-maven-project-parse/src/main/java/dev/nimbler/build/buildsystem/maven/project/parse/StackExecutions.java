package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenExecution;

final class StackExecutions extends StackBase {

    private final List<MavenExecution> executions;
    
    StackExecutions(Context context) {
        super(context);
        
        this.executions = new ArrayList<>();
    }

    void add(MavenExecution execution) {
        
        Objects.requireNonNull(execution);

        executions.add(execution);
    }
    
    List<MavenExecution> getExecutions() {
        return executions;
    }
}
