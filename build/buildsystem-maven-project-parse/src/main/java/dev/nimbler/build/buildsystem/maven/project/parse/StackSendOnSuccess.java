package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBoolean;

final class StackSendOnSuccess extends StackBoolean {

    StackSendOnSuccess(Context context) {
        super(context);
    }
}
