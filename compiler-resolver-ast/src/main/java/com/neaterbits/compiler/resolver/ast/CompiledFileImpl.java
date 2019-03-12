package com.neaterbits.compiler.resolver.ast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.FileSpec;

final class CompiledFileImpl<COMPLEXTYPE, COMPILATION_UNIT> implements CompiledFile<COMPLEXTYPE, COMPILATION_UNIT> {

	private final CompiledFileSpecImpl spec;
	private final COMPILATION_UNIT compilationUnit;
	private final List<CompiledType<COMPLEXTYPE>> types;
	
	CompiledFileImpl(CompiledFileSpecImpl spec, COMPILATION_UNIT compilationUnit, List<CompiledType<COMPLEXTYPE>> types) {
		
		Objects.requireNonNull(spec);
		Objects.requireNonNull(compilationUnit);
		Objects.requireNonNull(types);
		
		this.spec = spec;
		this.compilationUnit = compilationUnit;
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
	public COMPILATION_UNIT getCompilationUnit() {
		return compilationUnit;
	}

	@Override
	public String toString() {
		return "CompiledFileImpl [spec=" + spec + ", types=" + types + "]";
	}
}
