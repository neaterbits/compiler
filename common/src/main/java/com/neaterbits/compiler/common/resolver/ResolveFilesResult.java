package com.neaterbits.compiler.common.resolver;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.TypeDependency;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

public final class ResolveFilesResult {
	private final ResolvedTypeCodeMapImpl codeMap;
	private final ResolveState resolveState;
	
	ResolveFilesResult(ResolvedTypeCodeMapImpl codeMap, ResolveState resolveState) {
		
		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(resolveState);
		
		this.codeMap = codeMap;
		this.resolveState = resolveState;
	}

	public CodeMap getCodeMap() {
		return codeMap;
	}
	
	ResolvedType getType(ScopedName scopedName) {
		return codeMap.getType(scopedName);
	}
	
	Set<TypeDependency> getUnresolvedExtendsFrom(FileSpec fileSpec) {
		Objects.requireNonNull(fileSpec);
		
		if (!resolveState.hasFile(fileSpec)) {
			throw new IllegalArgumentException("File not resolved " + fileSpec);
		}
		
		return resolveState.getUnresolvedExtendsFrom(fileSpec);
	}

	Set<TypeDependency> getUnresolvedTypeDependencies(FileSpec fileSpec) {
		Objects.requireNonNull(fileSpec);
		
		if (!resolveState.hasFile(fileSpec)) {
			throw new IllegalArgumentException("File not resolved " + fileSpec);
		}
		
		return resolveState.getUnresolvedDependencies(fileSpec);
	}
	
	public Map<FileSpec, Set<TypeDependency>> getUnresolvedDependencies() {
		return resolveState.getAllUnresolvedDependencies();
	}

	public Collection<ResolvedFile> getResolvedFiles() {
		return resolveState.getResolvedFiles();
	}
}
