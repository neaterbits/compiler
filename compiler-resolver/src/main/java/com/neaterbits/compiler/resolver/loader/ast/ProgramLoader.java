package com.neaterbits.compiler.resolver.loader.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.parser.ParsedFile;
import com.neaterbits.compiler.resolver.loader.CompiledFile;
import com.neaterbits.compiler.resolver.loader.CompiledType;
import com.neaterbits.compiler.resolver.loader.LoadSpec;
import com.neaterbits.compiler.resolver.loader.Loader;

public class ProgramLoader implements Loader {

	private final List<CompiledFile> allFiles;
	
	public ProgramLoader(Program program) {
		
		Objects.requireNonNull(program);

		this.allFiles = new ArrayList<>();
		
		for (Module module : program.getModules()) {
			for (ParsedFile parsedFile : module.getParsedFiles()) {
				allFiles.add(makeCompiledFile(parsedFile));
			}
		}
	}

	private CompiledFile makeCompiledFile(ParsedFile parsedFile) {

		final CompiledFileSpecImpl compiledFileSpec = new CompiledFileSpecImpl(parsedFile.getFile().getPath());

		final List<CompiledType> types = TypeFinder.findTypes(parsedFile, compiledFileSpec);
		
		return new CompiledFileImpl(compiledFileSpec, new FileImports(parsedFile), types);
	}
	
	
	@Override
	public Collection<CompiledFile> getAllFiles() {
		return allFiles;
	}

	@Override
	public CompiledFile load(LoadSpec loadSpec) {
		
		
		return null;
	}
}
