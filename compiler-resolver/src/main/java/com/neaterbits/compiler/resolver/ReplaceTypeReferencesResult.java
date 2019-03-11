package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;

public final class ReplaceTypeReferencesResult<BUILTINTYPE, COMPLEXTYPE> {

	private final Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolvedFiles;
	private final ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> codeMap;
	private final Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> typesInDependencyOrder;

	public ReplaceTypeReferencesResult(
			Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolvedFiles,
			ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> codeMap,
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> typesInDependencyOrder) {

		Objects.requireNonNull(resolvedFiles);
		Objects.requireNonNull(codeMap);
		
		this.resolvedFiles = resolvedFiles;
		this.codeMap = codeMap;
		this.typesInDependencyOrder = typesInDependencyOrder;
	}
	
	public Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> getResolvedFiles() {
		return resolvedFiles;
	}

	public ResolvedTypeCodeMap<BUILTINTYPE, COMPLEXTYPE> getCodeMap() {
		return codeMap;
	}

	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getTypesInDependencyOrder() {
		return typesInDependencyOrder;
	}
}
