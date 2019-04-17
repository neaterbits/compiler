package com.neaterbits.compiler.resolver.passes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFile;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFiles;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiInputOutput;

public class ResolvedTypeDependencies<PARSED_FILE extends ParsedFile, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
			extends MultiInputOutput
			implements CompiledAndResolvedFiles {

	private final List<PARSED_FILE> parsedFiles;
	private final ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFilesResult;
	
	public ResolvedTypeDependencies(List<PARSED_FILE> parsedFiles,
			ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFilesResult) {
		this.parsedFiles = parsedFiles;
		this.resolveFilesResult = resolveFilesResult;
	}

	public List<PARSED_FILE> getParsedFiles() {
		return parsedFiles;
	}

	public ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> getResolveFilesResult() {
		return resolveFilesResult;
	}

	@Override
	public CompiledAndResolvedFile getFile(FileSpec fileSpec) {

		Objects.requireNonNull(fileSpec);
		
		final PARSED_FILE parsedFile = parsedFiles.stream()
				.filter(file -> file.getFileSpec().equals(fileSpec))
				.findFirst()
				.orElse(null);
		
		final CompiledAndResolvedFile result;
		
		if (parsedFile == null) {
			result = null;
		}
		else {

			result = new CompiledAndResolvedFile() {

				@Override
				public <AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type) {
					return parsedFile.getASTElements(type);
				}

				@Override
				public List<CompileError> getErrors() {
					
					final List<CompileError> allErrors = new ArrayList<>(parsedFile.getErrors());
					
					final List<ResolveError> resolveErrors = resolveFilesResult.getResolveErrors(parsedFile.getFileSpec());
					
					if (resolveErrors != null) {
						allErrors.addAll(resolveErrors);
					}
					
					return allErrors;
				}
			};
		}

		return result;
	}
}
