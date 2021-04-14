package dev.nimbler.build.model;

import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.resource.LibraryResourcePath;

final class LibraryDependencyImpl extends BaseDependencyWrapper<LibraryResourcePath> implements LibraryDependency {

	LibraryDependencyImpl(BaseDependency dependency) {
		super(dependency);
	}
}
