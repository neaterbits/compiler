package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.common.parse.StackEntity;

final class StackDistributionManagementRelocation extends StackEntity {

    private String message;

    StackDistributionManagementRelocation(Context context) {
        super(context);
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
