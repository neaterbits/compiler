package com.neaterbits.ide.common.project;

import java.util.Objects;

import com.neaterbits.build.types.resource.ResourcePath;

public class SourceFolder {
	
	private final ResourcePath resourcePath;

	public SourceFolder(ResourcePath resourcePath) {
		
		Objects.requireNonNull(resourcePath);
		
		this.resourcePath = resourcePath;
	}

	public ResourcePath getResourcePath() {
		return resourcePath;
	}
}
