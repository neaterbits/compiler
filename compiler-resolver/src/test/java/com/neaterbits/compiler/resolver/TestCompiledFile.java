package com.neaterbits.compiler.resolver;


import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.IFileImports;

public class TestCompiledFile extends BaseTestFile<CompiledType> implements CompiledFile {

	private final IFileImports imports;
	
	public TestCompiledFile(String name, IFileImports imports, CompiledType ... types) {
		super(name, types);

		this.imports = imports;
	}
	
	public TestCompiledFile(FileSpec fileSpec, IFileImports imports, CompiledType ... types) {
		super(fileSpec, types);

		this.imports = imports;
	}
	

	@Override
	public IFileImports getImports() {
		return imports;
	}
}
