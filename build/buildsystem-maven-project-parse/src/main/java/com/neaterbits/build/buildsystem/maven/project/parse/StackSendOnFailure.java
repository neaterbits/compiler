package com.neaterbits.build.buildsystem.maven.project.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBoolean;
import com.neaterbits.util.parse.context.Context;

final class StackSendOnFailure extends StackBoolean {

    StackSendOnFailure(Context context) {
        super(context);
    }
}
