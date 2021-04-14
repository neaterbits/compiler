package dev.nimbler.build.buildsystem.maven.plugins.descriptor.parse;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBoolean;

final class StackThreadSafe extends StackBoolean {

    StackThreadSafe(Context context) {
        super(context);
    }
}
