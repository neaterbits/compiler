package com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse;

import com.neaterbits.build.buildsystem.common.parse.StackBoolean;
import com.neaterbits.util.parse.context.Context;

final class StackRequiresDirectInvocation extends StackBoolean {

    StackRequiresDirectInvocation(Context context) {
        super(context);
    }
}
