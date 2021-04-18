package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.project.model.MavenExtension;

final class StackBuild extends StackBaseBuild implements OutputDirectorySetter {

    private String outputDirectory;
    private String sourceDirectory;
    private String scriptSourceDirectory;
    private String testSourceDirectory;
    
    private List<MavenExtension> extensions;

    StackBuild(Context context) {
		super(context);
	}

    final String getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    public final void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    String getSourceDirectory() {
        return sourceDirectory;
    }

    void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    String getScriptSourceDirectory() {
        return scriptSourceDirectory;
    }

    void setScriptSourceDirectory(String scriptSourceDirectory) {
        this.scriptSourceDirectory = scriptSourceDirectory;
    }

    String getTestSourceDirectory() {
        return testSourceDirectory;
    }

    void setTestSourceDirectory(String testSourceDirectory) {
        this.testSourceDirectory = testSourceDirectory;
    }

    final List<MavenExtension> getExtensions() {
        return extensions;
    }

    final void setExtensions(List<MavenExtension> extensions) {
        this.extensions = extensions;
    }
}
