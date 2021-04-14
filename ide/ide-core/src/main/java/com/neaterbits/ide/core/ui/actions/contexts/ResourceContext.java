package com.neaterbits.ide.core.ui.actions.contexts;

import java.util.Objects;

import com.neaterbits.build.types.resource.ResourcePath;

public abstract class ResourceContext<T extends ResourcePath> {

	private final T resource;

	public ResourceContext(T resource) {

		Objects.requireNonNull(resource);
		
		this.resource = resource;
	}

	public final T getResource() {
		return resource;
	}
}
