package dev.nimbler.build.types.resource;

import java.util.List;

public abstract class DirectoryResourcePath extends FileSystemResourcePath {

	public DirectoryResourcePath(DirectoryResourcePath resourcePath, FileResource resource) {
		super(resourcePath, resource);
	}

	public DirectoryResourcePath(DirectoryResourcePath resourcePath, DirectoryResource resource) {
		super(resourcePath, resource);
	}

	protected DirectoryResourcePath(DirectoryResourcePath resourcePath, FileSystemResource resource) {
		super(resourcePath, resource);
	}

	public DirectoryResourcePath(List<? extends Resource> path) {
		super(path);
	}

	public DirectoryResourcePath(Resource... resources) {
		super(resources);
	}
}
