package com.neaterbits.compiler.common.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.common.ModuleSpec;
import com.neaterbits.compiler.common.ast.Module;

final class ModuleParser {

	private final DirectoryParser directoryParser;
	
	ModuleParser(DirectoryParser directoryParser) {

		Objects.requireNonNull(directoryParser);

		this.directoryParser = directoryParser;
	}

	List<Module> parseModules(Collection<ModuleSpec> modules) {

		final Set<ModuleSpec> parsedModules = new HashSet<>();
		
		final Set<ModuleSpec> toParse = new HashSet<>(modules);
		
		final List<Module> parsed = new ArrayList<>();
		
		while (!toParse.isEmpty()) {

			final Iterator<ModuleSpec> iter = toParse.iterator();
			
			while (iter.hasNext()) {
				final ModuleSpec moduleSpec = iter.next();
				
				if (parsedModules.contains(moduleSpec)) {
					iter.remove();
				}
				else {
					final List<ParsedFile> parsedFiles = directoryParser.parseDirectory(moduleSpec.getBaseDirectory());
					
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
