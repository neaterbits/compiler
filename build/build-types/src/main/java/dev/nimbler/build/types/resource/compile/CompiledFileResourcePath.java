package dev.nimbler.build.types.resource.compile;

import dev.nimbler.build.types.resource.FileResourcePath;
import dev.nimbler.build.types.resource.ResourcePath;

public final class CompiledFileResourcePath extends FileResourcePath {

	public CompiledFileResourcePath(TargetDirectoryResourcePath resourcePath, CompiledFileResource resource) {
		super(resourcePath, resource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}
