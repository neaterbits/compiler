package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;

final class StackFile extends StackBase {

    private String exists;
    
    private String missing;

    StackFile(Context context) {
        super(context);
    }

    String getExists() {
        return exists;
    }

    void setExists(String exists) {
        this.exists = exists;
    }

    String getMissing() {
        return missing;
    }

    void setMissing(String missing) {
        this.missing = missing;
    }
}
