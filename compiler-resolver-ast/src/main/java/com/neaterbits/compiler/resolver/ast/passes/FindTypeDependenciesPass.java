package com.neaterbits.compiler.resolver.ast.passes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.resolver.ast.ProgramLoader;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledFiles;
import com.neaterbits.compiler.util.passes.ParsedFiles;
import com.neaterbits.compiler.util.passes.SingleToMultiPass;

public class FindTypeDependenciesPass 
	extends SingleToMultiPass<ASTParsedFile, CompiledFiles<CompilationUnit, ASTParsedFile>> {

	@Override
	public CompiledFiles<CompilationUnit, ASTParsedFile> execute(Collection<ASTParsedFile> input) throws IOException {
		
		final List<CompiledFile<CompilationUnit>> compiledFiles
				= new ArrayList<>(input.size());
		
		for (ASTParsedFile parsedFile : input) {
			final CompiledFile<CompilationUnit> compiledFile = ProgramLoader.makeCompiledFile(parsedFile);

			compiledFiles.add(compiledFile);
		}
		
		return new CompiledFiles<>(
				new ParsedFiles<>(new ArrayList<>(input)),
				compiledFiles);
	}
}
