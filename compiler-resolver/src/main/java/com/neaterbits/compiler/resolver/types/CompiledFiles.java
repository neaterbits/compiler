package com.neaterbits.compiler.resolver.types;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.util.passes.MultiFileInputOutput;

public class CompiledFiles<COMPLEXTYPE, COMPILATION_UNIT, PARSED_FILE> extends MultiFileInputOutput<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> {

	private final List<PARSED_FILE> parsedFiles;
	private final List<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> compiledFiles;
	
	public CompiledFiles(List<PARSED_FILE> parsedFiles, List<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> compiledFiles) {
		
		this.parsedFiles = Collections.unmodifiableList(parsedFiles);
		this.compiledFiles = Collections.unmodifiableList(compiledFiles);
	}
	
	public List<PARSED_FILE> getParsedFiles() {
		return parsedFiles;
	}

	public List<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> getCompiledFiles() {
		return compiledFiles;
	}

}
