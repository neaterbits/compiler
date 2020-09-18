package com.neaterbits.compiler.resolver.types;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.model.common.passes.MultiFileInputOutput;
import com.neaterbits.compiler.model.common.passes.ParsedFiles;
import com.neaterbits.compiler.util.parse.ParsedFile;

public class CompiledFiles<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> 

	extends MultiFileInputOutput<CompiledFile<COMPILATION_UNIT>> {

	private final ParsedFiles<PARSED_FILE> parsedFiles;
	private final List<CompiledFile<COMPILATION_UNIT>> compiledFiles;
	
	public CompiledFiles(ParsedFiles<PARSED_FILE> parsedFiles, List<CompiledFile<COMPILATION_UNIT>> compiledFiles) {
		
		this.parsedFiles = parsedFiles;
		this.compiledFiles = Collections.unmodifiableList(compiledFiles);
	}
	
	public ParsedFiles<PARSED_FILE> getParsedFiles() {
		return parsedFiles;
	}

	public List<CompiledFile<COMPILATION_UNIT>> getCompiledFiles() {
		return compiledFiles;
	}

}
