package com.neaterbits.compiler.resolver.types;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiFileInputOutput;
import com.neaterbits.compiler.util.passes.ParsedFiles;

public class CompiledFiles<COMPLEXTYPE, COMPILATION_UNIT, PARSED_FILE extends ParsedFile> 

	extends MultiFileInputOutput<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> {

	private final ParsedFiles<PARSED_FILE> parsedFiles;
	private final List<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> compiledFiles;
	
	public CompiledFiles(ParsedFiles<PARSED_FILE> parsedFiles, List<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> compiledFiles) {
		
		this.parsedFiles = parsedFiles;
		this.compiledFiles = Collections.unmodifiableList(compiledFiles);
	}
	
	public ParsedFiles<PARSED_FILE> getParsedFiles() {
		return parsedFiles;
	}

	public List<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> getCompiledFiles() {
		return compiledFiles;
	}

}
