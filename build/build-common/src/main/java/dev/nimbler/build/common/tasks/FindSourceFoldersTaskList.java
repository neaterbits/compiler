package dev.nimbler.build.common.tasks;

import java.util.List;

import org.jutils.concurrency.scheduling.task.TaskList;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

final class FindSourceFoldersTaskList extends TaskList<BuildRoot, ProjectModuleResourcePath, List<SourceFolderResourcePath>> {

	FindSourceFoldersTaskList(BuildRoot buildRoot) {
		super(
			buildRoot,
			BuildRoot::getModules,
			(br, module) -> br.getBuildSystemRootScan().findSourceFolders(module),
			(br, module, sourceFolders) -> br.setSourceFolders(module, sourceFolders));
	}
}
