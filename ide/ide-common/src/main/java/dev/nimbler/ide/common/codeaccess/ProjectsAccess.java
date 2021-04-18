package dev.nimbler.ide.common.codeaccess;

import java.util.Collection;
import java.util.List;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public interface ProjectsAccess {

	@FunctionalInterface
	public interface ProjectsListener {

		void onSourceFoldersChanged(ProjectModuleResourcePath module);
	}
	
	Collection<ProjectModuleResourcePath> getModules();

	List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module);

	void addProjectsListener(ProjectsListener listener);
	
	SourceFolderResourcePath findSourceFolder(String projectName, String sourceFolder);
}
