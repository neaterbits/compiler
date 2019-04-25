package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.UserDefinedType;

public class TestCompiledFile extends BaseTestFile<CompiledType<UserDefinedType>>
		implements CompiledFile<UserDefinedType, CompilationUnit> {

	private final CompilationUnit compilationUnit;
	
	@SafeVarargs
	public TestCompiledFile(String name, CompilationUnit compilationUnit, CompiledType<UserDefinedType> ... types) {
		super(name, types);

		this.compilationUnit = compilationUnit;
	}
	
	@SafeVarargs
	public TestCompiledFile(FileSpec fileSpec, CompilationUnit compilationUnit, CompiledType<UserDefinedType> ... types) {
		super(fileSpec, types);

		this.compilationUnit = compilationUnit;
	}

	@Override
	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}
}
