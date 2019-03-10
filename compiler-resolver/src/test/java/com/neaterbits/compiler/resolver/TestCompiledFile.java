package com.neaterbits.compiler.resolver;


import com.neaterbits.compiler.resolver.loader.CompiledFile;
import com.neaterbits.compiler.resolver.loader.CompiledType;
import com.neaterbits.compiler.resolver.loader.FileSpec;
import com.neaterbits.compiler.resolver.loader.IFileImports;

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
