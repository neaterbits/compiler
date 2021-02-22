package com.neaterbits.compiler.common.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.TypeDependency;

final class ResolveState {

	private final Map<FileSpec, CompiledFile> toProcess;
	private final Map<FileSpec, FileUnresolvedReferences> unresolvedReferences;
	private final Map<FileSpec, FileCachedResolvedTypes> cachedResolvedTypes;
	private final Map<FileSpec, ResolvedFile> resolved;
	
	ResolveState(Collection<CompiledFile> startFiles) {
		this.toProcess = new HashMap<>();
		this.unresolvedReferences = new HashMap<>();
		this.cachedResolvedTypes = new HashMap<>();
		this.resolved = new HashMap<>();

		for (CompiledFile startFile : startFiles) {
			toProcess.put(startFile.getSpec(), startFile);
		}
	}
	
	void forEachUnresolved(Consumer<FileSpec> forEach) {
		unresolvedReferences.keySet().forEach(forEach);
	}
	
	boolean hasFile(FileSpec fileSpec) {
		Objects.requireNonNull(fileSpec);
		
		return    toProcess.containsKey(fileSpec)
			   || unresolvedReferences.containsKey(fileSpec)
			   || resolved.containsKey(fileSpec);
	}
	
	FileUnresolvedReferences getUnresolvedReferences(FileSpec fileSpec) {
		Objects.requireNonNull(fileSpec);
		
		return unresolvedReferences.get(fileSpec);
	}
	
	boolean hasLeftToProcess() {
		return !toProcess.isEmpty();
	}
	
	CompiledFile getNextToProcess() {
		return toProcess.values().iterator().next();
	}
	
	boolean hasUnresolvedFile(FileSpec fileSpec) {
		return unresolvedReferences.containsKey(fileSpec);
	}

	boolean isResolved(FileSpec fileSpec) {
		return resolved.containsKey(fileSpec);
	}
	
	void addResolved(ResolvedFile resolvedFile) {
		Objects.requireNonNull(resolvedFile);

		resolved.put(resolvedFile.getSpec(), resolvedFile);
	}

	void addUnresolvedReferences(FileSpec fileSpec, FileUnresolvedReferences unresolvedReferences) {
		
		Objects.requireNonNull(fileSpec);
		Objects.requireNonNull(unresolvedReferences);

		this.unresolvedReferences.put(fileSpec, unresolvedReferences);
	}
	
	Set<ScopedName> getUnresolvedExtendsFrom(FileSpec fileSpec) {
		
		final FileUnresolvedReferences refs = unresolvedReferences.get(fileSpec);
		
		Set<ScopedName> extendsFrom = null;
		
		if (refs != null) {
			extendsFrom = refs.getExtendsFrom();
		}
		
		return extendsFrom != null ? extendsFrom : Collections.emptySet();
	}
	
	Set<TypeDependency> getUnresolvedDependencies(FileSpec fileSpec) {
		
		final FileUnresolvedReferences refs = unresolvedReferences.get(fileSpec);
		
		Set<TypeDependency> dependencies = null;
		
		if (refs != null) {
			dependencies = refs.getDependencies();
		}
		
		return dependencies != null ? dependencies : Collections.emptySet();
	}

	FileCachedResolvedTypes getCache(FileSpec fileSpec) {
		
		Objects.requireNonNull(fileSpec);
		
		FileCachedResolvedTypes cache = cachedResolvedTypes.get(fileSpec);
		
		if (cache == null) {
			cache = new FileCachedResolvedTypes();
			
			cachedResolvedTypes.put(fileSpec, cache);
		}
		
		return cache;
	}

	void removeFromToProcess(FileSpec fileSpec) {

		toProcess.remove(fileSpec);
	}
}
