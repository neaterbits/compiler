package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;

final class ResolvedFileImpl<BUILTINTYPE, COMPLEXTYPE> implements ResolvedFile<BUILTINTYPE, COMPLEXTYPE> {

	private final FileSpec fileSpec;
	private final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> resolvedTypes;
	
	ResolvedFileImpl(FileSpec fileSpec, List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> resolvedTypes) {
		
		Objects.requireNonNull(fileSpec);

		this.fileSpec = fileSpec;
		this.resolvedTypes = Collections.unmodifiableList(resolvedTypes);
	}

	@Override
	public String getName() {
		return fileSpec.getName();
	}

	@Override
	public FileSpec getSpec() {
		return fileSpec;
	}

	@Override
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getTypes() {
		return resolvedTypes;
	}
}
