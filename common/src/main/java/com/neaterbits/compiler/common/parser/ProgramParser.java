package com.neaterbits.compiler.common.parser;

import java.util.List;

import com.neaterbits.compiler.common.ModuleSpec;
import com.neaterbits.compiler.common.ast.Module;
import com.neaterbits.compiler.common.ast.Program;

public final class ProgramParser {

	private final ModuleParser moduleParser;
	
	public ProgramParser(DirectoryParser directoryParser) {
		this.moduleParser = new ModuleParser(directoryParser);
	}
	
	public Program parseProgram(List<ModuleSpec> modules) {

		final List<Module> parsedModules = moduleParser.parseModules(modules);

		return new Program(parsedModules);
	}
}
