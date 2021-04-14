package dev.nimbler.build.model;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public interface BuildRootListener {
	
	void onSourceFoldersChanged(ProjectModuleResourcePath module);

}
