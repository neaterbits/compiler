package dev.nimbler.build.buildsystem.maven.project.model;

import dev.nimbler.build.buildsystem.maven.common.model.MavenEntity;
import dev.nimbler.build.buildsystem.maven.common.model.MavenModuleId;

public final class MavenExtension extends MavenEntity {

	public MavenExtension(MavenModuleId moduleId) {
		super(moduleId, null);
	}
}
