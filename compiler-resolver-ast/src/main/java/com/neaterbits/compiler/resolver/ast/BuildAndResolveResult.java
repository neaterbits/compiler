package com.neaterbits.compiler.resolver.ast;

import java.util.List;

import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UserDefinedType;

public class BuildAndResolveResult {

	private final List<ASTParsedFile> parsedFiles;
	private final ResolveFilesResult<BuiltinType, UserDefinedType, TypeName> resolveFilesResult;

	public BuildAndResolveResult(List<ASTParsedFile> parsedFiles,
			ResolveFilesResult<BuiltinType, UserDefinedType, TypeName> resolveFilesResult) {

		this.parsedFiles = parsedFiles;
		this.resolveFilesResult = resolveFilesResult;
	}

	public List<ASTParsedFile> getParsedFiles() {
		return parsedFiles;
	}

	public ResolveFilesResult<BuiltinType, UserDefinedType, TypeName> getResolveFilesResult() {
		return resolveFilesResult;
	}
}
