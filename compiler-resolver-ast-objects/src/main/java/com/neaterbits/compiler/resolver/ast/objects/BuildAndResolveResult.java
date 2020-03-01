package com.neaterbits.compiler.resolver.ast.objects;

import java.util.List;

import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.resolver.ResolveFilesResult;

public class BuildAndResolveResult {

	private final List<ASTParsedFile> parsedFiles;
	private final ResolveFilesResult resolveFilesResult;

	public BuildAndResolveResult(List<ASTParsedFile> parsedFiles,
			ResolveFilesResult resolveFilesResult) {

		this.parsedFiles = parsedFiles;
		this.resolveFilesResult = resolveFilesResult;
	}

	public List<ASTParsedFile> getParsedFiles() {
		return parsedFiles;
	}

	public ResolveFilesResult getResolveFilesResult() {
		return resolveFilesResult;
	}
}
