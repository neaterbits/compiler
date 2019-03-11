package com.neaterbits.compiler.resolver.ast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.IFileImports;

final class CompiledFileImpl<COMPLEXTYPE> implements CompiledFile<COMPLEXTYPE> {

	private final CompiledFileSpecImpl spec;
	private final IFileImports fileImports;
	private final List<CompiledType<COMPLEXTYPE>> types;
	
	CompiledFileImpl(CompiledFileSpecImpl spec, IFileImports fileImports, List<CompiledType<COMPLEXTYPE>> types) {
		
		Objects.requireNonNull(spec);
		Objects.requireNonNull(fileImports);
		Objects.requireNonNull(types);
		
		this.spec = spec;
		this.fileImports = fileImports;
		this.types = Collections.unmodifiableList(types);
	}

	@Override
	public String getName() {
		return spec.getName();
	}

	@Override
	public FileSpec getSpec() {
		return spec;
	}

	@Override
	public Collection<CompiledType<COMPLEXTYPE>> getTypes() {
		return types;
	}

	@Override
	public IFileImports getImports() {
		return fileImports;
	}

	@Override
	public String toString() {
		return "CompiledFileImpl [spec=" + spec + ", types=" + types + "]";
	}
}
