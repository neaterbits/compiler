package dev.nimbler.build.buildsystem.maven.plugins.descriptor.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBoolean;

final class StackThreadSafe extends StackBoolean {

    StackThreadSafe(Context context) {
        super(context);
    }
}
