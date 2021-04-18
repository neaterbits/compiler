package dev.nimbler.ide.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.build.model.BuildRootListener;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.ide.common.codeaccess.ProjectsAccess.ProjectsListener;

final class ProjectsListeners {

	private final List<ProjectsListener> listeners;

	private final BuildRootListener buildRootListener;
	
	ProjectsListeners() {

		this.listeners = new ArrayList<>();

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
				listeners.forEach(l -> l.onSourceFoldersChanged(module));
			}
		};
	}
	
	void onModuleAdded(ProjectModuleResourcePath module) {
		
		Objects.requireNonNull(module);

		listeners.forEach(l -> l.onModuleAdded(module));
	}

	void onModuleRemoved(ProjectModuleResourcePath module) {

		Objects.requireNonNull(module);

		listeners.forEach(l -> l.onModuleRemoved(module));
	}

	void addListener(ProjectsListener listener) {
		
		Objects.requireNonNull(listener);
		
		listeners.add(listener);
	}

	void removeListener(ProjectsListener listener) {
		
		Objects.requireNonNull(listener);
		
		listeners.remove(listener);
	}

	BuildRootListener getBuildRootListener() {
		return buildRootListener;
	}
}
