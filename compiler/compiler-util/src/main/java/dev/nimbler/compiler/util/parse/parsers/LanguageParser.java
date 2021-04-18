package dev.nimbler.compiler.util.parse.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import org.jutils.parse.ParserException;

import dev.nimbler.compiler.util.parse.ParseError;
import dev.nimbler.compiler.util.parse.Parser.CreateParseLogger;

public interface LanguageParser<COMPILATION_UNIT> {

	boolean canParseFile(File file);

	COMPILATION_UNIT parse(
	        InputStream inputStream,
	        Charset charset,
	        Collection<ParseError> errors,
	        String file,
	        CreateParseLogger createParseLogger)
	                throws IOException, ParserException;
}
