package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.TypeName;

public final class ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE> {
	
	private final List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolvedFiles;
	
	// private final ResolveState resolveState;
	private final ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE> resolvedTypesMap;
	private final BuiltinTypesMap<BUILTINTYPE> builtinTypesMap;
	private final UnresolvedDependencies unresolvedDependencies;
	
	private final Collection<BUILTINTYPE> builtinTypes;
	
	ResolveFilesResult(
			List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolvedFiles,
			ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE> resolvedTypesMap,
			BuiltinTypesMap<BUILTINTYPE> builtinTypesMap,
			Collection<BUILTINTYPE> builtinTypes,
			UnresolvedDependencies unresolvedDependencies) {
		
		Objects.requireNonNull(resolvedFiles);
		
		Objects.requireNonNull(resolvedTypesMap);
		Objects.requireNonNull(builtinTypesMap);
		Objects.requireNonNull(builtinTypes);
		
		Objects.requireNonNull(unresolvedDependencies);
		
		
		this.resolvedFiles = Collections.unmodifiableList(resolvedFiles);
		
		// this.resolveState = resolveState;
		this.resolvedTypesMap = resolvedTypesMap;
		this.builtinTypesMap = builtinTypesMap;
		this.unresolvedDependencies = unresolvedDependencies;
		
		this.builtinTypes = builtinTypes;
	}

	public List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> getResolvedFiles() {
		return resolvedFiles;
	}

	
	ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE> getResolvedTypesMap() {
		return resolvedTypesMap;
	}
	
	BuiltinTypesMap<BUILTINTYPE> getBuiltinTypesMap() {
		return builtinTypesMap;
	}

	public ResolvedType<BUILTINTYPE, COMPLEXTYPE> getType(TypeName completeName) {
		
		Objects.requireNonNull(completeName);
		
		return resolvedTypesMap.lookupType(completeName);
	}

	public UnresolvedDependencies getUnresolvedDependencies() {
		return unresolvedDependencies;
	}

	Collection<BUILTINTYPE> getBuiltinTypes() {
		return builtinTypes;
	}
	
	/*
	Set<CompiledTypeDependency> getUnresolvedExtendsFrom(FileSpec fileSpec) {
		Objects.requireNonNull(fileSpec);
		
		if (!resolveState.hasFile(fileSpec)) {
			throw new IllegalArgumentException("File not resolved " + fileSpec);
		}
		
		return resolveState.getUnresolvedExtendsFrom(fileSpec);
	}

	Set<CompiledTypeDependency> getUnresolvedTypeDependencies(FileSpec fileSpec) {
		Objects.requireNonNull(fileSpec);
		
		if (!resolveState.hasFile(fileSpec)) {
			throw new IllegalArgumentException("File not resolved " + fileSpec);
		}
		
		return resolveState.getUnresolvedDependencies(fileSpec);
	}
	
	public Map<FileSpec, Set<CompiledTypeDependency>> getUnresolvedDependencies() {
		return resolveState.getAllUnresolvedDependencies();
	}

	public Collection<ResolvedFile> getResolvedFiles() {
		return resolveState.getResolvedFiles();
	}
	*/
}
