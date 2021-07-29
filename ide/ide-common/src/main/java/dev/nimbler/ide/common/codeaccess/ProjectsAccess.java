package dev.nimbler.ide.common.codeaccess;

import java.util.List;

import dev.nimbler.build.model.BuildRoot.DependencySelector;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;

public interface ProjectsAccess {

	public interface ProjectsListener {

		void onModuleAdded(ProjectModuleResourcePath module);
		
		void onModuleRemoved(ProjectModuleResourcePath module);

		void onSourceFoldersChanged(ProjectModuleResourcePath module);
	}
	
	List<ProjectModuleResourcePath> getRootModules();
	
	List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module);

	void addProjectsListener(ProjectsListener listener);
	
	SourceFolderResourcePath findSourceFolder(String projectName, String sourceFolder);

	RuntimeEnvironment getRuntimeEnvironment(ProjectModuleResourcePath module);
	
	TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath module);

    List<ProjectDependency> getTransitiveProjectDependenciesForProjectModule(
            ProjectModuleResourcePath module,
            DependencySelector selector);

    List<LibraryDependency> getTransitiveLibraryDependenciesForProjectModule(
            ProjectModuleResourcePath module,
            DependencySelector selector);
	
}
