package com.neaterbits.compiler.ast.objects.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;

public interface LanguageParser {

	boolean canParseFile(File file);

	CompilationUnit parse(InputStream inputStream, Charset charset, Collection<ParseError> errors, String file, ParseLogger parseLogger) throws IOException;
	
}
