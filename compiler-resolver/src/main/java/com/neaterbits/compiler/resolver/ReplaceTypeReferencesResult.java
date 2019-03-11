package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;

public final class ReplaceTypeReferencesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	private final Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles;
	private final ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap;
	private final Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder;

	public ReplaceTypeReferencesResult(
			Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles,
			ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap,
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder) {

		Objects.requireNonNull(resolvedFiles);
		Objects.requireNonNull(codeMap);
		
		this.resolvedFiles = resolvedFiles;
		this.codeMap = codeMap;
		this.typesInDependencyOrder = typesInDependencyOrder;
	}
	
	public Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getResolvedFiles() {
		return resolvedFiles;
	}

	public ResolvedTypeCodeMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> getCodeMap() {
		return codeMap;
	}

	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getTypesInDependencyOrder() {
		return typesInDependencyOrder;
	}
}
