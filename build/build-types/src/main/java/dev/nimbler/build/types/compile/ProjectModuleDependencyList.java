package dev.nimbler.build.types.compile;

import java.util.List;

import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public final class ProjectModuleDependencyList extends ModuleDependencyList<ProjectModuleResourcePath, ProjectDependency> {

	public ProjectModuleDependencyList(ProjectModuleResourcePath module, List<ProjectDependency> dependencies) {
		super(module, dependencies);
	}
}
