package dev.nimbler.build.types.compile;

import java.util.List;

import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public final class ExternalModuleDependencyList
		extends ModuleDependencyList<LibraryResourcePath, LibraryDependency> {

	public ExternalModuleDependencyList(ProjectModuleResourcePath module, List<LibraryDependency> dependencies) {
		super(module, dependencies);
	}
}
