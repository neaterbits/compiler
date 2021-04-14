package dev.nimbler.build.types.dependencies;

import java.util.Set;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public interface DependencyMap {

	Set<ProjectModuleResourcePath> findLeafModules();
	
	Set<ProjectModuleResourcePath> getDependencies(ProjectModuleResourcePath resourcePath);
	
}
