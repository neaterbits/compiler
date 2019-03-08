package com.neaterbits.compiler.common.resolver;


import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.IFileImports;
import com.neaterbits.compiler.common.loader.FileSpec;

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
