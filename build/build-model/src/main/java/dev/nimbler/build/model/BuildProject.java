package dev.nimbler.build.model;

import java.util.List;
import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFolderResourcePath;

final class BuildProject<PROJECT> {

	private final PROJECT buildSystemProject;
	
	private List<SourceFolderResourcePath> sourceFolders;

	BuildProject(PROJECT buildSystemProject) {

		Objects.requireNonNull(buildSystemProject);
		
		this.buildSystemProject = buildSystemProject;
	}

	PROJECT getBuildSystemProject() {
		return buildSystemProject;
	}
	
	List<SourceFolderResourcePath> getSourceFolders() {
		return sourceFolders;
	}

	void setSourceFolders(List<SourceFolderResourcePath> sourceFolders) {
		this.sourceFolders = sourceFolders;
	}
}
