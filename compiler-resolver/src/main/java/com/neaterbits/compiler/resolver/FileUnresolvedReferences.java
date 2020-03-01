package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;

final class FileUnresolvedReferences<COMPILATION_UNIT> {
	private final CompiledFile<COMPILATION_UNIT> fileToResolve;
	private final Set<CompiledTypeDependency> extendsFrom;
	private final Set<CompiledTypeDependency> dependencies;
	
	FileUnresolvedReferences(
			CompiledFile<COMPILATION_UNIT> fileToResolve,
			Collection<CompiledTypeDependency> extendsFrom,
			Collection<CompiledTypeDependency> dependencies) {
		
		Objects.requireNonNull(fileToResolve);
		
		this.fileToResolve = fileToResolve;
		
		this.extendsFrom 	= extendsFrom != null  ? new HashSet<>(extendsFrom)  : Collections.emptySet();
		this.dependencies 	= dependencies != null ? new HashSet<>(dependencies) : Collections.emptySet();
	}

	CompiledFile<COMPILATION_UNIT> getFileToResolve() {
		return fileToResolve;
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
