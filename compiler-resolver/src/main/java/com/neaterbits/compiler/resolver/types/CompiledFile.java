package com.neaterbits.compiler.resolver.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;

public class CompiledFile<COMPILATION_UNIT> implements FileInfo {

	private final FileSpec spec;
	private final COMPILATION_UNIT compilationUnit;
	private final List<CompiledType> types;
	
	public CompiledFile(FileSpec spec, COMPILATION_UNIT compilationUnit, CompiledType type) {
		this(spec, compilationUnit, Arrays.asList(type));
		
		Objects.requireNonNull(type);
	}

	public CompiledFile(FileSpec spec, COMPILATION_UNIT compilationUnit, List<CompiledType> types) {
		
		Objects.requireNonNull(spec);
		Objects.requireNonNull(compilationUnit);
		Objects.requireNonNull(types);
		
		this.spec = spec;
		this.compilationUnit = compilationUnit;
		this.types = Collections.unmodifiableList(types);
	}

	@Override
	public String getName() {
		return spec.getParseContextName();
	}

	@Override
	public FileSpec getSpec() {
		return spec;
	}

	public Collection<CompiledType> getTypes() {
		return types;
	}

	public COMPILATION_UNIT getCompilationUnit() {
		return compilationUnit;
	}

	@Override
	public String toString() {
		return "CompiledFile [spec=" + spec + ", types=" + types + "]";
	}
}
