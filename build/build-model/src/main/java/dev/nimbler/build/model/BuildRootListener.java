package dev.nimbler.build.model;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public interface BuildRootListener {

	void onModuleAdded(ProjectModuleResourcePath module);
	
	void onModuleRemoved(ProjectModuleResourcePath module);
	
	void onSourceFoldersChanged(ProjectModuleResourcePath module);

}
