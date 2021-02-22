package com.neaterbits.compiler.common.resolver;


import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.FileImports;
import com.neaterbits.compiler.common.loader.FileSpec;

public class TestCompiledFile extends BaseTestFile<CompiledType> implements CompiledFile {

	private final FileImports imports;
	
	public TestCompiledFile(String name, FileImports imports, CompiledType ... types) {
		super(name, types);

		this.imports = imports;
	}
	
	public TestCompiledFile(FileSpec fileSpec, FileImports imports, CompiledType ... types) {
		super(fileSpec, types);

		this.imports = imports;
	}
	

	@Override
	public FileImports getImports() {
		return imports;
	}
}
