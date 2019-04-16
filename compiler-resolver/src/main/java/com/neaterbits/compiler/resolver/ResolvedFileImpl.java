package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.FileSpec;

final class ResolvedFileImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> implements ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	private final FileSpec fileSpec;
	private final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedTypes;
	
	ResolvedFileImpl(FileSpec fileSpec, List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedTypes) {
		
		Objects.requireNonNull(fileSpec);

		this.fileSpec = fileSpec;
		this.resolvedTypes = Collections.unmodifiableList(resolvedTypes);
	}

	@Override
	public String getName() {
		return fileSpec.getParseContextName();
	}

	@Override
	public FileSpec getSpec() {
		return fileSpec;
	}

	@Override
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getTypes() {
		return resolvedTypes;
	}
}
