package dev.nimbler.build.buildsystem.maven.plugins.descriptor.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBoolean;

final class StackInheritedByDefault extends StackBoolean {

    StackInheritedByDefault(Context context) {
        super(context);
    }
}
