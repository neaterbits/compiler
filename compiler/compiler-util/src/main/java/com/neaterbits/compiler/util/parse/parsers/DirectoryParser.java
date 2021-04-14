package com.neaterbits.compiler.util.parse.parsers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.FileSystemFileSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.IOError;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.Parser.CreateParseLogger;
import com.neaterbits.util.Files;
import com.neaterbits.util.parse.ParserException;

public abstract class DirectoryParser<COMPILATION_UNIT, PARSED_FILE> {

	private final List<LanguageParser<COMPILATION_UNIT>> parsers;
	
	protected abstract PARSED_FILE createParsedFile(FileSpec file, List<CompileError> errors, String log, COMPILATION_UNIT parsed);

	@SafeVarargs
    public DirectoryParser(LanguageParser<COMPILATION_UNIT> ... parsers) {
		this(Arrays.asList(parsers));
	}

	public DirectoryParser(List<LanguageParser<COMPILATION_UNIT>> parsers) {

		Objects.requireNonNull(parsers);

		this.parsers = parsers;
	}
	
	private LanguageParser<COMPILATION_UNIT> findParser(File file) {

		for (LanguageParser<COMPILATION_UNIT> parser : parsers) {
			if (parser.canParseFile(file)) {
				return parser;
			}
		}

		return null;
	}
	
	public final List<PARSED_FILE> parseDirectory(File directory, Charset charset) {
		return parseDirectory(directory, charset, null);
	}

	public final List<PARSED_FILE> parseDirectory(File directory, Charset charset, CreateParseLogger debugParseLogger) {

		final List<PARSED_FILE> parsedFiles = new ArrayList<>();
		
		Files.recurseDirectories(directory, file -> {

			final LanguageParser<COMPILATION_UNIT> parser = findParser(file);
			if (parser != null) {

				final List<CompileError> allFileerrors = new ArrayList<>();

				COMPILATION_UNIT compilationUnit = null;
				
				String log = null;

				try (FileInputStream fileInputStream = new FileInputStream(file)) {

					System.out.println("## compiling " + file.getName());
					
					final Collection<ParseError> parseErrors = new ArrayList<>();

					final ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					final PrintStream printStream = new PrintStream(baos);
					
					compilationUnit = parser.parse(
					        fileInputStream,
					        charset,
					        parseErrors,
					        file.getName(),
				            debugParseLogger != null
				                ? debugParseLogger
		                        : fullContextProvider -> new ParseLogger(System.out, fullContextProvider));
					
					printStream.flush();
					
					log = new String(baos.toByteArray());

					allFileerrors.addAll(parseErrors);
					
				} catch (IOException | ParserException ex) {
					allFileerrors.add(new IOError(file, "Failed to load file", ex));
				}
				
				final PARSED_FILE parsedFile = createParsedFile(
						new FileSystemFileSpec(file),
						allFileerrors,
						log,
						compilationUnit);
				
				parsedFiles.add(parsedFile);
			}
		});

		return parsedFiles;
	}
}
