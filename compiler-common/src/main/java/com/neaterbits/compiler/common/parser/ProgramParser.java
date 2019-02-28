package com.neaterbits.compiler.common.parser;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import com.neaterbits.compiler.common.ModuleSpec;
import com.neaterbits.compiler.common.ast.Module;
import com.neaterbits.compiler.common.ast.Program;
import com.neaterbits.compiler.common.log.ParseLogger;

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
