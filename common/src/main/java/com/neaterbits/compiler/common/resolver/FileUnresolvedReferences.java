package com.neaterbits.compiler.common.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;

final class FileUnresolvedReferences {
	private final CompiledFile fileToResolve;
	private final Set<CompiledTypeDependency> extendsFrom;
	private final Set<CompiledTypeDependency> dependencies;
	
	FileUnresolvedReferences(CompiledFile fileToResolve, Collection<CompiledTypeDependency> extendsFrom, Collection<CompiledTypeDependency> dependencies) {
		
		Objects.requireNonNull(fileToResolve);
		
		this.fileToResolve = fileToResolve;
		
		this.extendsFrom 	= extendsFrom != null  ? new HashSet<>(extendsFrom)  : Collections.emptySet();
		this.dependencies 	= dependencies != null ? new HashSet<>(dependencies) : Collections.emptySet();
	}

	CompiledFile getFileToResolve() {
		return fileToResolve;
	}

	void removeExtendsFrom(ScopedName scopedName) {
		
		Objects.requireNonNull(scopedName);

		extendsFrom.remove(scopedName);
	}

	void removeDependency(CompiledTypeDependency dependency) {
		
		Objects.requireNonNull(dependency);
		
		dependencies.remove(dependency);
	}
	
	Set<CompiledTypeDependency> getExtendsFrom() {
		return Collections.unmodifiableSet(extendsFrom);
	}
	
	Set<CompiledTypeDependency> getDependencies() {
		return Collections.unmodifiableSet(dependencies);
	}

	boolean isEmpty() {
		return extendsFrom.isEmpty() && dependencies.isEmpty();
	}
}
