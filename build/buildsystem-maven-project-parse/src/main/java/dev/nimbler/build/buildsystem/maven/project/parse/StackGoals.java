package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;

final class StackGoals extends StackBase {

    private final List<String> goals;
    
    StackGoals(Context context) {
        super(context);
        
        this.goals = new ArrayList<>();
    }

    void add(String goal) {

        Objects.requireNonNull(goal);

        goals.add(goal);
    }
    
    List<String> getGoals() {
        return goals;
    }
}
