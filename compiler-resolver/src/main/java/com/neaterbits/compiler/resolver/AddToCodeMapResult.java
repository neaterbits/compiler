package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.resolver.passes.ResolvedFiles;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class AddToCodeMapResult<PARSED_FILE extends ParsedFile, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		extends ResolvedFiles<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	private final Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles;
	private final ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap;
	private final Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder;

	public AddToCodeMapResult(
			ResolvedFiles<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> other,
			Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles,
			ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> codeMap,
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typesInDependencyOrder) {

		super(other);
		
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
