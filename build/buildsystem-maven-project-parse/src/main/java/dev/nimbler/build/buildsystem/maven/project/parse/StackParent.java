package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.common.parse.StackEntity;

final class StackParent extends StackEntity {

    private String relativePath;
	
    StackParent(Context context) {
		super(context);
	}

    String getRelativePath() {
        return relativePath;
    }

    void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
