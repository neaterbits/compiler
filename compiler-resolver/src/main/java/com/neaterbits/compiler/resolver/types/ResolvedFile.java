package com.neaterbits.compiler.resolver.types;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;

public class ResolvedFile implements FileInfo {

	private final FileSpec fileSpec;
	private final List<ResolvedType> resolvedTypes;
	
	public ResolvedFile(FileSpec fileSpec, List<ResolvedType> resolvedTypes) {
		
		Objects.requireNonNull(fileSpec);

		this.fileSpec = fileSpec;
		this.resolvedTypes = Collections.unmodifiableList(resolvedTypes);
	}

	@Override
	public final String getName() {
		return fileSpec.getParseContextName();
	}

	@Override
	public final FileSpec getSpec() {
		return fileSpec;
	}

	public final Collection<ResolvedType> getTypes() {
		return resolvedTypes;
	}

	@Override
	public String toString() {
		return "ResolvedFile [fileSpec=" + fileSpec + ", resolvedTypes=" + resolvedTypes + "]";
	}
}
