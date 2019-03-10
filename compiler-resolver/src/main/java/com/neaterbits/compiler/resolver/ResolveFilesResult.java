package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;

public final class ResolveFilesResult {
	
	private final List<ResolvedFile> resolvedFiles;
	
	// private final ResolveState resolveState;
	private final ResolvedTypesMap resolvedTypesMap;
	private final BuiltinTypesMap builtinTypesMap;
	private final UnresolvedDependencies unresolvedDependencies;
	
	private final Collection<? extends BuiltinType> builtinTypes;
	
	ResolveFilesResult(
			List<ResolvedFile> resolvedFiles,
			ResolvedTypesMap resolvedTypesMap,
			BuiltinTypesMap builtinTypesMap,
			Collection<? extends BuiltinType> builtinTypes,
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

	public List<ResolvedFile> getResolvedFiles() {
		return resolvedFiles;
	}

	
	ResolvedTypesMap getResolvedTypesMap() {
		return resolvedTypesMap;
	}
	
	BuiltinTypesMap getBuiltinTypesMap() {
		return builtinTypesMap;
	}

	public ResolvedType getType(CompleteName completeName) {
		
		Objects.requireNonNull(completeName);
		
		return resolvedTypesMap.lookupType(completeName);
	}

	public UnresolvedDependencies getUnresolvedDependencies() {
		return unresolvedDependencies;
	}

	Collection<? extends BuiltinType> getBuiltinTypes() {
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
