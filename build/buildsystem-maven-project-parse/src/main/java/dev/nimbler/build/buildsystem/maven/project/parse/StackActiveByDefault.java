package dev.nimbler.build.buildsystem.maven.project.parse;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBoolean;

final class StackActiveByDefault extends StackBoolean {

    StackActiveByDefault(Context context) {
        super(context);
    }
}
