package com.neaterbits.compiler.ast.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.antlr4.AntlrParser;
import com.neaterbits.compiler.antlr4.ModelParserListener;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.util.TokenSequenceNoGenerator;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSourceInputStream;
import com.neaterbits.util.io.strings.StringSource;

public final class FileTypeParser<LISTENER extends ModelParserListener<CompilationUnit>>
		implements LanguageParser {

	@FunctionalInterface
	public interface ParserListenerFactory<LISTENER> {
		LISTENER create(StringSource stringSource, ParseLogger logger, TokenSequenceNoGenerator gen);
	}
	
	private final AntlrParser<CompilationUnit, LISTENER> parser;
	private final ParserListenerFactory<LISTENER> makeListener;
	private final String [] fileExtensions;

	public FileTypeParser(
			AntlrParser<CompilationUnit, LISTENER> parser,
			ParserListenerFactory<LISTENER> makeListener,
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
	public CompilationUnit parse(InputStream inputStream, Charset charset, Collection<ParseError> errors, String file, ParseLogger parseLogger) throws IOException {
		
		final TokenSequenceNoGenerator gen = new TokenSequenceNoGenerator();
		
		final LISTENER listener = makeListener.create(
				new StringSourceInputStream(inputStream, charset),
				parseLogger,
				gen);
		
		final Collection<ParseError> antlrErrors = parser.parse(inputStream, listener, file, parseLogger);

		errors.addAll(antlrErrors);
		
		return listener.getResult();
	}
}
