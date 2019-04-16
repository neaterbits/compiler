package com.neaterbits.compiler.resolver.ast;

import java.util.List;

import com.neaterbits.compiler.ast.parser.ParsedFile;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.util.TypeName;

public class BuildAndResolveResult {

	private final List<ParsedFile> parsedFiles;
	private final ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveFilesResult;

	public BuildAndResolveResult(List<ParsedFile> parsedFiles,
			ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveFilesResult) {

		this.parsedFiles = parsedFiles;
		this.resolveFilesResult = resolveFilesResult;
	}

	public List<ParsedFile> getParsedFiles() {
		return parsedFiles;
	}

	public ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> getResolveFilesResult() {
		return resolveFilesResult;
	}
}
