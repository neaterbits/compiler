package com.neaterbits.compiler.util.parse.parsers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.coll.MapOfList;

public final class ProgramParser<COMPILATION_UNIT, PARSED_FILE> {
    
    public static class Program<COMPILATION_UNIT, PARSED_FILE> {

        private final MapOfList<File, PARSED_FILE> parsedFiles;

        Program(MapOfList<File, PARSED_FILE> parsedFiles) {
            this.parsedFiles = parsedFiles;
        }
        
        public List<PARSED_FILE> getModuleParsedFiles(File module) {
            Objects.requireNonNull(module);
            
            return Collections.unmodifiableList(parsedFiles.get(module));
        }
    }

	private final ModuleParser<COMPILATION_UNIT, PARSED_FILE> moduleParser;
	
	public ProgramParser(DirectoryParser<COMPILATION_UNIT, PARSED_FILE> directoryParser) {
		this.moduleParser = new ModuleParser<>(directoryParser);
	}
	
	public Program<COMPILATION_UNIT, PARSED_FILE> parseProgram(
	        MapOfList<File, File> modules,
	        Charset charset,
	        File systemModule,
	        Consumer<Collection<PARSED_FILE>> postProcessSystemModule,
	        ParseLogger debugParseLogger) throws IOException {

		final MapOfList<File, PARSED_FILE> parsedModules
		                    = moduleParser.parseModules(
		                            modules,
		                            charset,
		                            systemModule,
		                            postProcessSystemModule,
		                            debugParseLogger);

		return new Program<>(parsedModules);
	}
}
