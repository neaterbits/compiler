package com.neaterbits.compiler.common.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.compiler.common.antlr4.AntlrError;
import com.neaterbits.compiler.common.antlr4.AntlrParser;
import com.neaterbits.compiler.common.antlr4.ModelParserListener;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.log.ParseLogger;

public final class FileTypeParser<LISTENER extends ModelParserListener<CompilationUnit>>
		implements LanguageParser {

	private final AntlrParser<CompilationUnit, LISTENER> parser;
	private final Function<ParseLogger, LISTENER> makeListener;
	private final String [] fileExtensions;

	public FileTypeParser(
			AntlrParser<CompilationUnit, LISTENER> parser,
			Function<ParseLogger, LISTENER> makeListener,
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
	public CompilationUnit parse(InputStream inputStream, Collection<AntlrError> errors, ParseLogger parseLogger) throws IOException {
		final LISTENER listener = makeListener.apply(parseLogger);
		
		final Collection<AntlrError> antlrErrors = parser.parse(inputStream, listener, parseLogger);

		errors.addAll(antlrErrors);
		
		return listener.getResult();
	}
}
