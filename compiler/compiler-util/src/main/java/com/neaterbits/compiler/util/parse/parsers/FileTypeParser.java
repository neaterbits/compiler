package com.neaterbits.compiler.util.parse.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.compiler.util.parse.Parser.CreateParseLogger;
import com.neaterbits.util.parse.ParserException;

public final class FileTypeParser<COMPILATION_UNIT> implements LanguageParser<COMPILATION_UNIT> {

	private final Parser<COMPILATION_UNIT> parser;
	private final String [] fileExtensions;

	public FileTypeParser(Parser<COMPILATION_UNIT> parser, String ... fileExtensions) {

		Objects.requireNonNull(parser);

		this.parser = parser;

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
	public COMPILATION_UNIT parse(
	        InputStream inputStream,
	        Charset charset,
	        Collection<ParseError> errors,
	        String file,
	        CreateParseLogger createParseLogger) throws IOException, ParserException {
		
		final Collection<ParseError> compileErrors = new ArrayList<>();
		        
        final COMPILATION_UNIT result = parser.parse(
                inputStream,
                charset,
                compileErrors,
                file,
                createParseLogger);

		errors.addAll(compileErrors);
		
		return result;
	}
}
