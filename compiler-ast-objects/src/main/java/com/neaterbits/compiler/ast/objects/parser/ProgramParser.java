package com.neaterbits.compiler.ast.objects.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;

import com.neaterbits.compiler.ast.objects.Module;
import com.neaterbits.compiler.ast.objects.Program;
import com.neaterbits.compiler.util.modules.ModuleSpec;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class ProgramParser {

	private final ModuleParser moduleParser;
	
	public ProgramParser(DirectoryParser directoryParser) {
		this.moduleParser = new ModuleParser(directoryParser);
	}
	
	public Program parseProgram(List<ModuleSpec> modules, Charset charset, ModuleSpec systemModule, Consumer<Module> postProcessSystemModule, ParseLogger debugParseLogger) throws IOException {

		final List<Module> parsedModules = moduleParser.parseModules(modules, charset, systemModule, postProcessSystemModule, debugParseLogger);

		return new Program(parsedModules);
	}
}