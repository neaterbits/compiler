package com.neaterbits.compiler.common.resolver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.FileSpec;

public final class UnresolvedDependencies {

	private final Map<FileSpec, Set<CompiledTypeDependency>> map;
	
	UnresolvedDependencies() {
		this.map = new HashMap<>();
	}
	
	void add(FileSpec fileSpec, CompiledTypeDependency dependency) {

		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(dependency);

		Set<CompiledTypeDependency> dependencies = map.get(fileSpec);
		
		if (dependencies == null) {
			dependencies = new HashSet<>();

			map.put(fileSpec, dependencies);
		}

		dependencies.add(dependency);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}
}
