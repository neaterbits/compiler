package com.neaterbits.compiler.resolver.ast;

import java.util.List;

import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.util.TypeName;

public class BuildAndResolveResult {

	private final List<ASTParsedFile> parsedFiles;
	private final ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveFilesResult;

	public BuildAndResolveResult(List<ASTParsedFile> parsedFiles,
			ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveFilesResult) {

		this.parsedFiles = parsedFiles;
		this.resolveFilesResult = resolveFilesResult;
	}

	public List<ASTParsedFile> getParsedFiles() {
		return parsedFiles;
	}

	public ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> getResolveFilesResult() {
		return resolveFilesResult;
	}
}
