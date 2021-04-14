package dev.nimbler.build.model;

import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

final class ProjectDependencyImpl extends BaseDependencyWrapper<ProjectModuleResourcePath> implements ProjectDependency {

	ProjectDependencyImpl(BaseDependency dependency) {
		super(dependency);
	}
}
