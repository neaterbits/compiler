package com.neaterbits.compiler.common.resolver;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.TypeDependency;
import com.neaterbits.compiler.common.resolver.references.References;

public final class ResolveFilesResult {
	private final References references;
	private final ResolveState resolveState;
	
	ResolveFilesResult(References references, ResolveState resolveState) {
		
		Objects.requireNonNull(references);
		Objects.requireNonNull(resolveState);
		
		this.references = references;
		this.resolveState = resolveState;
	}

	public CodeMap getCodeMap() {
		return references;
	}
	
	ResolvedType getType(ScopedName scopedName) {
		return references.getType(scopedName);
	}
	
	Set<ScopedName> getUnresolvedExtendsFrom(FileSpec fileSpec) {
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
}
