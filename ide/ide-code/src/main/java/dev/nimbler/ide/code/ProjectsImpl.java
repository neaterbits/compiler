package dev.nimbler.ide.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.code.codemap.Projects;

final class ProjectsImpl implements Projects {
	
	private final ProjectsListeners projectsListeners;

	private final List<BuildRoot> buildRoots;
	
	ProjectsImpl(ProjectsListeners projectsListeners) {
		
		Objects.requireNonNull(projectsListeners);
		
		this.projectsListeners = projectsListeners;
		
		this.buildRoots = new ArrayList<>();
		
	}

	void addBuildRoot(BuildRoot buildRoot) {
		
		Objects.requireNonNull(buildRoot);
		
		if (buildRoots.contains(buildRoot)) {
			throw new IllegalArgumentException();
		}
		
		buildRoots.add(buildRoot);
		
		buildRoot.addListener(projectsListeners.getBuildRootListener());
	}
	
	void removeBuildRoot(BuildRoot buildRoot) {

		Objects.requireNonNull(buildRoot);
		
		if (!buildRoots.contains(buildRoot)) {
			throw new IllegalArgumentException();
		}
		
		buildRoot.removeListener(projectsListeners.getBuildRootListener());
		
		buildRoots.remove(buildRoot);
	}
	
	@Override
	public RuntimeEnvironment getRuntimeEnvironment(ProjectModuleResourcePath module) {

		return forEachBuildRoot(buildRoot -> buildRoot.getRuntimeEnvironment(module));
	}

	@Override
	public <T> T forEachSourceFolder(Function<SourceFolderResourcePath, T> function) {
		
		return forEachBuildRoot(buildRoot -> buildRoot.forEachSourceFolder(function));
	}
	
	List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module) {

		final BuildRoot buildRoot = findBuildRoot(module);
		
		return buildRoot != null ? buildRoot.getSourceFolders(module) : Collections.emptyList();
	}
	
	private BuildRoot findBuildRoot(ProjectModuleResourcePath module) {
		
		final BuildRoot buildRoot = forEachBuildRoot(br -> {
			
			final BuildRoot result;
			
			if (br.getModules().stream()
					.anyMatch(m -> m.equals(module))) {
				result = br;
			}
			else {
				result = null;
			}
			
			return result;
		});
		
		return buildRoot;
	}
	
	private <T> T forEachBuildRoot(Function<BuildRoot, T> function) {
		
		T result = null;
		
		for (BuildRoot buildRoot : buildRoots) {
			
			result = function.apply(buildRoot);
			
			if (result != null) {
				break;
			}
		}
		
		return result;
	}
}
