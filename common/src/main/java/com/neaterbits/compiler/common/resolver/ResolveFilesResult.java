package com.neaterbits.compiler.common.resolver;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

public final class ResolveFilesResult {
	
	private final List<ResolvedFile> resolvedFiles;
	
	private final ResolvedTypeCodeMapImpl codeMap;
	// private final ResolveState resolveState;
	private final ResolvedTypesMap resolvedTypes;
	private final UnresolvedDependencies unresolvedDependencies;
	
	ResolveFilesResult(List<ResolvedFile> resolvedFiles, ResolvedTypeCodeMapImpl codeMap, /* ResolveState resolveState, */ ResolvedTypesMap resolvedTypes, UnresolvedDependencies unresolvedDependencies) {
		
		Objects.requireNonNull(resolvedFiles);
		
		Objects.requireNonNull(codeMap);
		// Objects.requireNonNull(resolveState);
		Objects.requireNonNull(resolvedTypes);
		
		Objects.requireNonNull(unresolvedDependencies);
		
		this.resolvedFiles = Collections.unmodifiableList(resolvedFiles);
		
		this.codeMap = codeMap;
		// this.resolveState = resolveState;
		this.resolvedTypes = resolvedTypes;
		this.unresolvedDependencies = unresolvedDependencies;
	}

	public List<ResolvedFile> getResolvedFiles() {
		return resolvedFiles;
	}

	public CodeMap getCodeMap() {
		return codeMap;
	}

	ResolvedType getType(ScopedName scopedName) {
		return codeMap.getType(scopedName);
	}
	
	public ResolvedTypesMap getResolvedTypesMap() {
		return resolvedTypes;
	}

	public UnresolvedDependencies getUnresolvedDependencies() {
		return unresolvedDependencies;
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
