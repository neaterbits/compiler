package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackText;

final class StackProperty extends StackText {

    private final String name;

    StackProperty(Context context, String name) {
        super(context);

        Objects.requireNonNull(name);
        
        this.name = name;
    }

    String getName() {
        return name;
    }
}
