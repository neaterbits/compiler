package dev.nimbler.build.buildsystem.common;

import java.util.List;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public interface BuildSystemRootScan {

	List<SourceFolderResourcePath> findSourceFolders(ProjectModuleResourcePath moduleResourcePath);
	
}
