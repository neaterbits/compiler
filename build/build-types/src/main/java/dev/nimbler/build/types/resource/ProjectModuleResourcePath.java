package dev.nimbler.build.types.resource;

import java.util.List;

public final class ProjectModuleResourcePath extends ModuleResourcePath {

	public ProjectModuleResourcePath(ProjectModuleResourcePath resourcePath, ModuleResource resource) {
		super(resourcePath, resource);
	}

	public ProjectModuleResourcePath(List<ModuleResource> path) {
		super(path);
	}

	public ProjectModuleResourcePath(ModuleResource... resources) {
		super(resources);
	}

	public boolean isDirectSubModuleOf(ProjectModuleResourcePath other) {
		return isDirectSubPathOf(other);
	}

	@Override
	public ResourcePath getParentPath() {
		
		final ResourcePath parentPath = isAtRoot()
		        ? null
                : new ProjectModuleResourcePath(getPaths(1));

		return parentPath;
	}

	public ProjectModuleResourcePath getRoot() {
        
        ProjectModuleResourcePath path;
        
        for (path = this; !path.isAtRoot(); path = (ProjectModuleResourcePath)getParentPath()) {
            
        }
        
        return path;
    }

	public final String getName() {
		final ModuleResource moduleResource = (ModuleResource)getLast();

		return moduleResource.getName();
	}
}
