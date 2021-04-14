package dev.nimbler.build.common.compile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.ResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public final class PrintlnBuildLogger implements BuildLogger {
	@Override
	public void onScanModuleSourceFolders(ProjectModuleResourcePath module) {
		System.out.println("Scanning source folders for " + module.getName());
	}

	@Override
	public void onScanModuleSourceFoldersResult(ProjectModuleResourcePath module,
			List<SourceFolderResourcePath> sourceFolders) {
		
		System.out.println("Got source folders for " + module.getName() + ": " + names(sourceFolders));
	}

	@Override
	public void onScanModuleSourceFilesResult(
			ProjectModuleResourcePath module,
			List<SourceFileResourcePath> sourceFiles,
			List<SourceFileResourcePath> alreadyBuilt) {
		
		System.out.println("Got source files for " + module.getName() + ": "
					+ names(sourceFiles) + ", already built: " + names(alreadyBuilt));
		
	}
	
	@Override
	public void onBuildModules(Collection<ProjectModuleResourcePath> modules) {
		System.out.println("Build modules: " + names(modules));
	}

	private static List<String> names(Collection<? extends ResourcePath> resourcePaths) {
		return resourcePaths.stream()
				.map(resourcePath -> resourcePath.getLast().getName())
				.collect(Collectors.toList());
	}
}
