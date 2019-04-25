package com.neaterbits.compiler.resolver.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.UserDefinedType;

public class ProgramLoader  {

	public static Collection<CompiledFile<UserDefinedType, CompilationUnit>> getCompiledFiles(Program program) {
		final List<CompiledFile<UserDefinedType, CompilationUnit>> allFiles = new ArrayList<>();
		
		for (Module module : program.getModules()) {
			for (ASTParsedFile parsedFile : module.getParsedFiles()) {
				allFiles.add(makeCompiledFile(parsedFile));
			}
		}
		return allFiles;
	}

	public static CompiledFile<UserDefinedType, CompilationUnit> makeCompiledFile(ASTParsedFile parsedFile) {

		final FileSpec compiledFileSpec = parsedFile.getFileSpec();

		final List<CompiledType<UserDefinedType>> types = TypeFinder.findTypes(parsedFile, compiledFileSpec);
		
		return new CompiledFileImpl<UserDefinedType, CompilationUnit>(compiledFileSpec, parsedFile.getParsed(), types);
	}
}
