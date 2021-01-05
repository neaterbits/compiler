package com.neaterbits.compiler.java.compile.modulebuilder;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.java.compile.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.model.common.passes.ParsedFiles;
import com.neaterbits.compiler.util.parse.ParsedFile;

abstract class BaseMappedFiles<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
		extends ParsedFiles<PARSED_FILE>
		implements CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> {

	private final Map<String, Integer> sourceFileNos;

	BaseMappedFiles(ParsedFiles<PARSED_FILE> other, Map<String, Integer> sourceFileNos) {

		super(other);

		Objects.requireNonNull(sourceFileNos);
		
		if (other.getParsedFiles().size() != sourceFileNos.size()) {
		    throw new IllegalArgumentException();
		}

		this.sourceFileNos = sourceFileNos;
	}

	/*
	@Override
	public final int getSourceFileNo(CompiledAndResolvedFile file) {

		System.out.println("## get file " + file.getParsedFile().getFileSpec() + " from " + sourceFileNos);
		return sourceFileNos.get(file.getParsedFile().getFileSpec());
	}
	*/

	@Override
	public final int getSourceFileNo(String name) {

		Objects.requireNonNull(name);
		
		return sourceFileNos.get(name);
	}

	@Override
	public final COMPILATION_UNIT getCompilationUnit(String name) {
		return getFile(name).getParsedFile().getCompilationUnit();
	}
}
