package com.neaterbits.compiler.ast.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;

import com.neaterbits.compiler.antlr4.AntlrParser;
import com.neaterbits.compiler.antlr4.ModelParserListener;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.util.TokenSequenceNoGenerator;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class FileTypeParser<LISTENER extends ModelParserListener<CompilationUnit>>
		implements LanguageParser {

	private final AntlrParser<CompilationUnit, LISTENER> parser;
	private final BiFunction<ParseLogger, TokenSequenceNoGenerator, LISTENER> makeListener;
	private final String [] fileExtensions;

	public FileTypeParser(
			AntlrParser<CompilationUnit, LISTENER> parser,
			BiFunction<ParseLogger, TokenSequenceNoGenerator, LISTENER> makeListener,
			String ... fileExtensions) {

		Objects.requireNonNull(parser);
		Objects.requireNonNull(makeListener);

		this.parser = parser;
		this.makeListener = makeListener;

		this.fileExtensions = Arrays.copyOf(fileExtensions, fileExtensions.length);
	}
	
	private static boolean amongExtensions(File file, String [] fileExtensions) {
		
		for (String fileExtension : fileExtensions) {
			if (file.getName().endsWith(fileExtension)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canParseFile(File file) {
		return amongExtensions(file, fileExtensions);
	}

	
	@Override
	public CompilationUnit parse(InputStream inputStream, Collection<ParseError> errors, String file, ParseLogger parseLogger) throws IOException {
		
		final TokenSequenceNoGenerator gen = new TokenSequenceNoGenerator();
		
		final LISTENER listener = makeListener.apply(parseLogger, gen);
		
		final Collection<ParseError> antlrErrors = parser.parse(inputStream, listener, file, parseLogger);

		errors.addAll(antlrErrors);
		
		return listener.getResult();
	}
}
