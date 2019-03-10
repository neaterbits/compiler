package com.neaterbits.compiler.resolver.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.parser.ParsedFile;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;

public class ProgramLoader  {

	public static Collection<CompiledFile> getCompiledFiles(Program program) {
		final List<CompiledFile> allFiles = new ArrayList<>();
		
		for (Module module : program.getModules()) {
			for (ParsedFile parsedFile : module.getParsedFiles()) {
				allFiles.add(makeCompiledFile(parsedFile));
			}
		}
		return allFiles;
	}

	private static CompiledFile makeCompiledFile(ParsedFile parsedFile) {

		final CompiledFileSpecImpl compiledFileSpec = new CompiledFileSpecImpl(parsedFile.getFile().getPath());

		final List<CompiledType> types = TypeFinder.findTypes(parsedFile, compiledFileSpec);
		
		return new CompiledFileImpl(compiledFileSpec, new FileImports(parsedFile), types);
	}
}
