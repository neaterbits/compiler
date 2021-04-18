package dev.nimbler.build.buildsystem.maven.project.parse;

import java.io.File;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;

final class StackInputStreamPomEventListener extends BaseStackPomEventListener {

    private StackProject project;
    
    @Override
    public void onProjectEnd(Context context) {
        
        this.project = pop();
    }
    
    MavenProject makeProject(File rootDirectory) {
        return super.makeProject(project, rootDirectory);
    }
}
