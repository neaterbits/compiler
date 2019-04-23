package com.neaterbits.compiler.resolver.passes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFile;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFiles;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.ParsedFiles;

public abstract class PostResolveFiles<PARSED_FILE extends ParsedFile, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		extends ParsedFiles<PARSED_FILE>
		implements CompiledAndResolvedFiles {

	private final ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFilesResult;

	private final List<CompiledAndResolvedFile> files;
	
	protected PostResolveFiles(PostResolveFiles<PARSED_FILE, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> other) {
		this(other, other.resolveFilesResult);
	}

	protected PostResolveFiles(
			ParsedFiles<PARSED_FILE> other,
			ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFilesResult) {

		super(other);
		
		Objects.requireNonNull(resolveFilesResult);
		
		this.resolveFilesResult = resolveFilesResult;
		
		final List<PARSED_FILE> parsedFiles = getParsedFiles();
		
		this.files = new ArrayList<>(parsedFiles.size());
		
		for (PARSED_FILE parsedFile : parsedFiles) {

			final CompiledAndResolvedFile file = new CompiledAndResolvedFile() {

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

				@Override
				public ParsedFile getParsedFile() {
					return parsedFile;
				}
			};

			files.add(file);
		}
	}

	public final ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> getResolveFilesResult() {
		return resolveFilesResult;
	}

	@Override
	public final CompiledAndResolvedFile getFile(FileSpec fileSpec) {

		Objects.requireNonNull(fileSpec);

		return files.stream()
				.filter(file -> file.getParsedFile().getFileSpec().equals(fileSpec))
				.findFirst()
				.orElse(null);
	}

	@Override
	public List<CompiledAndResolvedFile> getFiles() {
		return Collections.unmodifiableList(files);
	}
}
