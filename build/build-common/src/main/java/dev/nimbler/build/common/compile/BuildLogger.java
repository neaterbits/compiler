package dev.nimbler.build.common.compile;

import java.util.Collection;
import java.util.List;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public interface BuildLogger {

	void onScanModuleSourceFolders(ProjectModuleResourcePath module);
	
	void onScanModuleSourceFoldersResult(ProjectModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders);
	
	void onScanModuleSourceFilesResult(
			ProjectModuleResourcePath module,
			List<SourceFileResourcePath> sourceFiles,
			List<SourceFileResourcePath> alreadyBuilt);
	
	void onBuildModules(Collection<ProjectModuleResourcePath> modules);
}
