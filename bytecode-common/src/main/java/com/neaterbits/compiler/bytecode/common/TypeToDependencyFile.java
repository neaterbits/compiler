package com.neaterbits.compiler.bytecode.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.common.TypeName;

public final class TypeToDependencyFile implements ClassLibs {

	private final Map<TypeName, DependencyFile> typeToDependency;
	
	public TypeToDependencyFile() {
		this.typeToDependency = new HashMap<>();
	}
	
	public void addModuleDependencyTypes(DependencyFile module, Set<TypeName> types) {
		addDependencyTypes(module, types);
	}
		
	public void addLibraryDependencyTypes(DependencyFile library, Set<TypeName> types) {
		addDependencyTypes(library, types);
	}

	private void addDependencyTypes(DependencyFile dependency, Set<TypeName> types) {
			
		Objects.requireNonNull(dependency);
		Objects.requireNonNull(types);

		for (TypeName type : types) {
			
			if (typeToDependency.put(type, dependency) != null) {
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public DependencyFile getDependencyFileFor(TypeName typeName) {
		return typeToDependency.get(typeName);
	}
}
