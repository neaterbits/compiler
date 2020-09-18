package com.neaterbits.compiler.resolver.passes;

import com.neaterbits.compiler.model.common.passes.ParsedFiles;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.util.parse.ParsedFile;

public class ResolvedTypeDependencies<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
			extends PostResolveFiles<PARSED_FILE, COMPILATION_UNIT> {

	public ResolvedTypeDependencies(ParsedFiles<PARSED_FILE> parsedFiles,
			ResolveFilesResult resolveFilesResult) {
		
		super(parsedFiles, resolveFilesResult);
	}
}
