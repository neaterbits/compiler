package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;

public final class ReplaceTypeReferencesResult {

	private final Collection<ResolvedFile> resolvedFiles;
	private final ResolvedTypeCodeMapImpl codeMap;
	private final Collection<ResolvedType> typesInDependencyOrder;

	public ReplaceTypeReferencesResult(Collection<ResolvedFile> resolvedFiles, ResolvedTypeCodeMapImpl codeMap, Collection<ResolvedType> typesInDependencyOrder) {

		Objects.requireNonNull(resolvedFiles);
		Objects.requireNonNull(codeMap);
		
		this.resolvedFiles = resolvedFiles;
		this.codeMap = codeMap;
		this.typesInDependencyOrder = typesInDependencyOrder;
	}
	
	public Collection<ResolvedFile> getResolvedFiles() {
		return resolvedFiles;
	}

	public ResolvedTypeCodeMap getCodeMap() {
		return codeMap;
	}

	public Collection<ResolvedType> getTypesInDependencyOrder() {
		return typesInDependencyOrder;
	}
}
