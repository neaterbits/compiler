package com.neaterbits.compiler.common.loader.ast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.IFileImports;
import com.neaterbits.compiler.common.loader.FileSpec;

final class CompiledFileImpl implements CompiledFile {

	private final CompiledFileSpecImpl spec;
	private final IFileImports fileImports;
	private final List<CompiledType> types;
	
	CompiledFileImpl(CompiledFileSpecImpl spec, IFileImports fileImports, List<CompiledType> types) {
		
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
	public Collection<CompiledType> getTypes() {
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
