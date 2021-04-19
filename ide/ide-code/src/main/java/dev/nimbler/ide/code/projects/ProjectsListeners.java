package dev.nimbler.ide.code.projects;

import java.util.Objects;

import dev.nimbler.build.model.BuildRootListener;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.ide.code.BaseListeners;
import dev.nimbler.ide.common.codeaccess.ProjectsAccess.ProjectsListener;

public final class ProjectsListeners extends BaseListeners<ProjectsListener> {

	private final BuildRootListener buildRootListener;
	
	public ProjectsListeners() {

		this.buildRootListener = new BuildRootListener() {

			@Override
			public void onModuleAdded(ProjectModuleResourcePath module) {
				onModuleAdded(module);
			}
			
			@Override
			public void onModuleRemoved(ProjectModuleResourcePath module) {
				onModuleRemoved(module);
			}
			
			@Override
			public void onSourceFoldersChanged(ProjectModuleResourcePath module) {
				forEach(l -> l.onSourceFoldersChanged(module));
			}
		};
	}
	
	void onModuleAdded(ProjectModuleResourcePath module) {
		
		Objects.requireNonNull(module);

		forEach(l -> l.onModuleAdded(module));
	}

	void onModuleRemoved(ProjectModuleResourcePath module) {

		Objects.requireNonNull(module);

		forEach(l -> l.onModuleRemoved(module));
	}

	BuildRootListener getBuildRootListener() {
		return buildRootListener;
	}
}
