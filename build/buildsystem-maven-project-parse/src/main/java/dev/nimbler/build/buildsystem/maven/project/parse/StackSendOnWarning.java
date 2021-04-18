package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBoolean;

final class StackSendOnWarning extends StackBoolean {

    StackSendOnWarning(Context context) {
        super(context);
    }
}
