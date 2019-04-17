package com.neaterbits.compiler.ast.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.util.FileSystemFileSpec;
import com.neaterbits.compiler.util.Files;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.IOError;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class DirectoryParser {

	private final List<LanguageParser> parsers;

	public DirectoryParser(LanguageParser ... parsers) {
		this(Arrays.asList(parsers));
	}

	public DirectoryParser(List<LanguageParser> parsers) {

		Objects.requireNonNull(parsers);

		this.parsers = parsers;
	}
	
	private LanguageParser findParser(File file) {

		for (LanguageParser parser : parsers) {
			if (parser.canParseFile(file)) {
				return parser;
			}
		}

		return null;
	}
	
	public List<ASTParsedFile> parseDirectory(File directory) {
		return parseDirectory(directory, null);
	}

	public List<ASTParsedFile> parseDirectory(File directory, ParseLogger debugParseLogger) {

		final List<ASTParsedFile> parsedFiles = new ArrayList<>();
		
		Files.recurseDirectories(directory, file -> {

			final LanguageParser parser = findParser(file);
			if (parser != null) {

				final List<CompileError> allFileerrors = new ArrayList<>();

				CompilationUnit compilationUnit = null;
				
				String log = null;

				try (FileInputStream fileInputStream = new FileInputStream(file)) {

					System.out.println("## compiling " + file.getName());
					
					final Collection<ParseError> parseErrors = new ArrayList<>();

					final ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					final PrintStream printStream = new PrintStream(baos);
					
					final ParseLogger parseLogger = new ParseLogger(printStream);
					
					compilationUnit = parser.parse(fileInputStream, parseErrors, file.getName(), debugParseLogger != null ? debugParseLogger : parseLogger);
					
					printStream.flush();
					
					log = new String(baos.toByteArray());

					allFileerrors.addAll(parseErrors);
					
				} catch (IOException ex) {
					allFileerrors.add(new IOError(file, "Failed to load file", ex));
				}
				
				final ASTParsedFile parsedFile = new ASTParsedFile(
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
