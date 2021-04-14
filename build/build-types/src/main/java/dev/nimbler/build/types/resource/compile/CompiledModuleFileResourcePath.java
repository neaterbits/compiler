package dev.nimbler.build.types.resource.compile;

import dev.nimbler.build.types.resource.FileResource;
import dev.nimbler.build.types.resource.FileResourcePath;
import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.ResourcePath;

// Path to eg. jar file
public final class CompiledModuleFileResourcePath extends FileResourcePath {

	public CompiledModuleFileResourcePath(ProjectModuleResourcePath resourcePath, FileResource resource) {
		super(resourcePath, resource);
	}

	public CompiledModuleFileResourcePath(LibraryResourcePath resourcePath, FileResource resource) {
		super(resourcePath, resource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}
