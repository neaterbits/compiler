package com.neaterbits.compiler.resolver;


import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.IFileImports;

public class TestCompiledFile extends BaseTestFile<CompiledType<ComplexType<?, ?, ?>>> implements CompiledFile<ComplexType<?, ?, ?>> {

	private final IFileImports imports;
	
	@SafeVarargs
	public TestCompiledFile(String name, IFileImports imports, CompiledType<ComplexType<?, ?, ?>> ... types) {
		super(name, types);

		this.imports = imports;
	}
	
	@SafeVarargs
	public TestCompiledFile(FileSpec fileSpec, IFileImports imports, CompiledType<ComplexType<?, ?, ?>> ... types) {
		super(fileSpec, types);

		this.imports = imports;
	}
	

	@Override
	public IFileImports getImports() {
		return imports;
	}
}
