package com.neaterbits.compiler.resolver.passes;

import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.ParsedFiles;

public class ResolvedTypeDependencies<PARSED_FILE extends ParsedFile, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
			extends ResolvedFiles<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	public ResolvedTypeDependencies(ParsedFiles<PARSED_FILE> parsedFiles,
			ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFilesResult) {
		
		super(parsedFiles, resolveFilesResult);
	}
}
