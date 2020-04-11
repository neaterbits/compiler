package com.neaterbits.compiler.resolver.ast.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Module;
import com.neaterbits.compiler.ast.objects.Program;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.util.FileSpec;

public class ProgramLoader  {

	public static Collection<CompiledFile<CompilationUnit>> getCompiledFiles(Program program) {
		final List<CompiledFile<CompilationUnit>> allFiles = new ArrayList<>();
		
		for (Module module : program.getModules()) {
			for (ASTParsedFile parsedFile : module.getParsedFiles()) {
				allFiles.add(makeCompiledFile(parsedFile));
			}
		}
		return allFiles;
	}

	public static CompiledFile<CompilationUnit> makeCompiledFile(ASTParsedFile parsedFile) {

		final FileSpec compiledFileSpec = parsedFile.getFileSpec();

		final List<CompiledType> types = TypeFinder.findTypes(parsedFile, compiledFileSpec);
		
		return new CompiledFile<CompilationUnit>(compiledFileSpec, parsedFile.getParsed(), types);
	}
}