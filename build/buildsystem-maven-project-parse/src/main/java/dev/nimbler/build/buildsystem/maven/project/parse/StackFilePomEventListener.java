package dev.nimbler.build.buildsystem.maven.project.parse;

import java.io.File;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;

final class StackFilePomEventListener extends BaseStackPomEventListener {

	private final File rootDirectory;

	private MavenProject mavenProject;

	StackFilePomEventListener(File rootDirectory) {

		Objects.requireNonNull(rootDirectory);

		this.rootDirectory = rootDirectory;
	}

	public MavenProject getMavenProject() {
		return mavenProject;
	}

	@Override
	public void onProjectEnd(Context context) {

		final StackProject project = pop();

		final MavenProject mavenProject = makeProject(project, rootDirectory);

		this.mavenProject = mavenProject;
	}
}
