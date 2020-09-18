package com.neaterbits.compiler.resolver.passes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.model.common.passes.ParsedFiles;
import com.neaterbits.compiler.model.common.passes.SingleToMultiPass;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledFiles;
import com.neaterbits.compiler.util.parse.ParsedFile;

public class FindTypeDependenciesPass<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> 
	extends SingleToMultiPass<PARSED_FILE, CompiledFiles<COMPILATION_UNIT, PARSED_FILE>> {

	@Override
	public CompiledFiles<COMPILATION_UNIT, PARSED_FILE> execute(Collection<PARSED_FILE> input) throws IOException {
		
		final List<CompiledFile<COMPILATION_UNIT>> compiledFiles
				= new ArrayList<>(input.size());
		
		for (PARSED_FILE parsedFile : input) {
			final CompiledFile<COMPILATION_UNIT> compiledFile = null; // ProgramLoader.makeCompiledFile(parsedFile);

			compiledFiles.add(compiledFile);
		}
		
		return new CompiledFiles<>(
				new ParsedFiles<>(new ArrayList<>(input)),
				compiledFiles);
	}
}
