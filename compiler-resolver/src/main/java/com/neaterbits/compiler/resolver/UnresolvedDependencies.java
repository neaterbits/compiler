package com.neaterbits.compiler.resolver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.util.FileSpec;

public final class UnresolvedDependencies {
	
	private int count;
	
	private final Map<FileSpec, Set<CompiledTypeDependency>> map;
	
	UnresolvedDependencies() {
		
		this.count = 0;
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

		if (dependencies.add(dependency)) {
			++ count;
		}
	}

	void remove(FileSpec fileSpec, CompiledTypeDependency dependency) {
		
		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(dependency);
		
		final Set<CompiledTypeDependency> dependencies = map.get(dependency);
		
		if (dependencies != null) {
			if (dependencies.remove(dependency)) {
				-- count;
			}
		}
	}
	
	public int getCount() {
		return count;
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public void forEach(BiConsumer<FileSpec, CompiledTypeDependency> each) {
		
		for (Map.Entry<FileSpec, Set<CompiledTypeDependency>> entry : map.entrySet()) {
			
			for (CompiledTypeDependency compiledTypeDependency : entry.getValue()) {
				each.accept(entry.getKey(), compiledTypeDependency);
			}
		}
	}

	@Override
	public String toString() {
		return "UnresolvedDependencies [count=" + count + ", map=" + map + "]";
	}
}
