package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.loader.FileSpec;
import com.neaterbits.compiler.resolver.loader.ResolvedFile;
import com.neaterbits.compiler.resolver.loader.ResolvedType;

final class ResolvedFileImpl implements ResolvedFile {

	private final FileSpec fileSpec;
	private final List<ResolvedType> resolvedTypes;
	
	ResolvedFileImpl(FileSpec fileSpec, List<ResolvedType> resolvedTypes) {
		
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
	public Collection<ResolvedType> getTypes() {
		return resolvedTypes;
	}
}
