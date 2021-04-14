package dev.nimbler.compiler.util.parse.parsers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.neaterbits.util.coll.MapOfList;

import dev.nimbler.compiler.util.parse.ParseLogger;
import dev.nimbler.compiler.util.parse.Parser.CreateParseLogger;

final class ModuleParser<COMPILATION_UNIT, PARSED_FILE> {

	private final DirectoryParser<COMPILATION_UNIT, PARSED_FILE> directoryParser;
	
	ModuleParser(DirectoryParser<COMPILATION_UNIT, PARSED_FILE> directoryParser) {

		Objects.requireNonNull(directoryParser);

		this.directoryParser = directoryParser;
	}

	MapOfList<File, PARSED_FILE> parseModules(
			MapOfList<File, File> modulesAndDependencies,
			Charset charset,
			File systemModule,
			Consumer<Collection<PARSED_FILE>> postProcessSystemModule,
			CreateParseLogger debugParseLogger) throws IOException {

		final Set<File> parsedModules = new HashSet<>();
		final Set<File> toParse = new HashSet<>(modulesAndDependencies.keys());
		
		final MapOfList<File, PARSED_FILE> parsed = new MapOfList<>();
		
		final String systemModulePath;
		
		if (systemModule != null) {
			systemModulePath = systemModule.getCanonicalPath();

			final List<PARSED_FILE> parsedFiles = directoryParser.parseDirectory(
					systemModule,
					charset,
					debugParseLogger);

			if (postProcessSystemModule != null) {
				postProcessSystemModule.accept(parsedFiles);
			}

			parsed.addCollection(systemModule, parsedFiles);
		}
		else {
			systemModulePath = null;
		}

		while (!toParse.isEmpty()) {

			final Iterator<File> iter = toParse.iterator();
			
			while (iter.hasNext()) {
				final File moduleSpec = iter.next();
		
				final String modulePath = moduleSpec.getCanonicalPath();

				if (systemModulePath != null && modulePath.startsWith(systemModulePath)) {
					throw new IllegalArgumentException("Module path within system module: " + modulePath);
				}
				
				if (parsedModules.contains(moduleSpec)) {
					iter.remove();
				}
				else {
					final List<PARSED_FILE> parsedFiles
					    = directoryParser.parseDirectory(moduleSpec, charset, debugParseLogger);
					
					parsedModules.add(moduleSpec);
					
					parsed.addCollection(moduleSpec, parsedFiles);
					
					for (File dependency : modulesAndDependencies.get(moduleSpec)) {
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
