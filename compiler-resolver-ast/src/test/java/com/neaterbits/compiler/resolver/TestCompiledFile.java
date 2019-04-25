package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.util.FileSpec;

public class TestCompiledFile extends BaseTestFile<CompiledType>
		implements CompiledFile<CompilationUnit> {

	private final CompilationUnit compilationUnit;
	
	@SafeVarargs
	public TestCompiledFile(String name, CompilationUnit compilationUnit, CompiledType ... types) {
		super(name, types);

		this.compilationUnit = compilationUnit;
	}
	
	@SafeVarargs
	public TestCompiledFile(FileSpec fileSpec, CompilationUnit compilationUnit, CompiledType ... types) {
		super(fileSpec, types);

		this.compilationUnit = compilationUnit;
	}

	@Override
	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}
}
