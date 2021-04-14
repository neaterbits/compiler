package dev.nimbler.build.buildsystem.common;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public interface BuildSystemRootListener {

	void onSourceFoldersChanged(ProjectModuleResourcePath module);

}
