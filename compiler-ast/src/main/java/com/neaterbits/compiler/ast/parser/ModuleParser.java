package com.neaterbits.compiler.ast.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.util.modules.ModuleSpec;
import com.neaterbits.compiler.util.parse.ParseLogger;

final class ModuleParser {

	private final DirectoryParser directoryParser;
	
	ModuleParser(DirectoryParser directoryParser) {

		Objects.requireNonNull(directoryParser);

		this.directoryParser = directoryParser;
	}

	List<Module> parseModules(
			Collection<ModuleSpec> modules,
			Charset charset,
			ModuleSpec systemModule,
			Consumer<Module> postProcessSystemModule,
			ParseLogger debugParseLogger) throws IOException {

		final Set<ModuleSpec> parsedModules = new HashSet<>();
		
		final Set<ModuleSpec> toParse = new HashSet<>(modules);
		
		final List<Module> parsed = new ArrayList<>();
		
		final String systemModulePath;
		
		if (systemModule != null) {
			systemModulePath = systemModule.getBaseDirectory().getCanonicalPath();

			final List<ASTParsedFile> parsedFiles = directoryParser.parseDirectory(
					systemModule.getBaseDirectory(),
					charset,
					debugParseLogger);
			
			final Module module = new Module(systemModule, parsedFiles);
			
			if (postProcessSystemModule != null) {
				postProcessSystemModule.accept(module);
			}

			parsed.add(module);
		}
		else {
			systemModulePath = null;
		}

		while (!toParse.isEmpty()) {

			final Iterator<ModuleSpec> iter = toParse.iterator();
			
			while (iter.hasNext()) {
				final ModuleSpec moduleSpec = iter.next();
		
				final String modulePath = moduleSpec.getBaseDirectory().getCanonicalPath();

				if (systemModulePath != null && modulePath.startsWith(systemModulePath)) {
					throw new IllegalArgumentException("Module path within system module: " + modulePath);
				}
				
				if (parsedModules.contains(moduleSpec)) {
					iter.remove();
				}
				else {
					final List<ASTParsedFile> parsedFiles = directoryParser.parseDirectory(moduleSpec.getBaseDirectory(), charset, debugParseLogger);
					
					final Module module = new Module(moduleSpec, parsedFiles);
					
					parsedModules.add(moduleSpec);
					
					parsed.add(module);
					
					for (ModuleSpec dependency : moduleSpec.getDependencies()) {
						if (!parsedModules.contains(dependency)) {
							toParse.add(dependency);
						}
					}
				}
			}
		}
		
		return parsed;
	}
}
