package com.neaterbits.compiler.ast.parser;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.util.modules.ModuleSpec;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class ProgramParser {

	private final ModuleParser moduleParser;
	
	public ProgramParser(DirectoryParser directoryParser) {
		this.moduleParser = new ModuleParser(directoryParser);
	}
	
	public Program parseProgram(List<ModuleSpec> modules, ModuleSpec systemModule, Consumer<Module> postProcessSystemModule, ParseLogger debugParseLogger) throws IOException {

		final List<Module> parsedModules = moduleParser.parseModules(modules, systemModule, postProcessSystemModule, debugParseLogger);

		return new Program(parsedModules);
	}
}
