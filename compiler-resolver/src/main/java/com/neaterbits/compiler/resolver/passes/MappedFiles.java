package com.neaterbits.compiler.resolver.passes;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.model.common.CompiledAndResolvedFile;
import com.neaterbits.compiler.model.common.passes.ParsedFiles;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.ParsedFile;

@Deprecated
public abstract class MappedFiles<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
		extends ParsedFiles<PARSED_FILE>
		implements CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> {

	private final Map<FileSpec, Integer> sourceFileNos;

	protected MappedFiles(
			ParsedFiles<PARSED_FILE> other,
			Map<FileSpec, Integer> sourceFileNos) {

		super(other);

		Objects.requireNonNull(sourceFileNos);

		this.sourceFileNos = sourceFileNos;
	}

	@Override
	public final int getSourceFileNo(CompiledAndResolvedFile file) {

		System.out.println("## get file " + file.getParsedFile().getFileSpec() + " from " + sourceFileNos);
		return sourceFileNos.get(file.getParsedFile().getFileSpec());
	}

	@Override
	public final int getSourceFileNo(FileSpec fileSpec) {

		Objects.requireNonNull(fileSpec);

		return sourceFileNos.get(fileSpec);
	}

	@Override
	public final COMPILATION_UNIT getCompilationUnit(FileSpec fileSpec) {
		return getFile(fileSpec).getParsedFile().getCompilationUnit();
	}
}
