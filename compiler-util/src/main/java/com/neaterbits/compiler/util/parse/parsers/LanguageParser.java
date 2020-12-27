package com.neaterbits.compiler.util.parse.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.Parser.CreateParseLogger;
import com.neaterbits.util.parse.ParserException;

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
