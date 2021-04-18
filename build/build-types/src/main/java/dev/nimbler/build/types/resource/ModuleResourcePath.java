package dev.nimbler.build.types.resource;

import java.util.List;

import dev.nimbler.build.types.ModuleId;

public abstract class ModuleResourcePath extends DirectoryResourcePath {

	public abstract String getName();

	protected ModuleResourcePath(ModuleResourcePath resourcePath, ModuleResource resource) {
		super(resourcePath, resource);
	}

	public ModuleResourcePath(List<? extends Resource> path) {
		super(path);
	}

	public ModuleResourcePath(Resource... resources) {
		super(resources);
	}

	public final ModuleId getModuleId() {
		return ((ModuleResource)getLast()).getModuleId();
	}
}
