package dev.nimbler.build.types.resource.compile;

import dev.nimbler.build.types.resource.DirectoryResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.ResourcePath;

public final class TargetDirectoryResourcePath extends DirectoryResourcePath {

	public TargetDirectoryResourcePath(ProjectModuleResourcePath moduleResourcePath, TargetDirectoryResource targetDirectoryResource) {
		super(moduleResourcePath, targetDirectoryResource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}
