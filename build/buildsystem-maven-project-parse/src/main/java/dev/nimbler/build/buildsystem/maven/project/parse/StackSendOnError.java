package dev.nimbler.build.buildsystem.maven.project.parse;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBoolean;

final class StackSendOnError extends StackBoolean {

    StackSendOnError(Context context) {
        super(context);
    }
}