package dev.nimbler.build.buildsystem.maven.plugins.descriptor.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBoolean;

final class StackRequiresDirectInvocation extends StackBoolean {

    StackRequiresDirectInvocation(Context context) {
        super(context);
    }
}
