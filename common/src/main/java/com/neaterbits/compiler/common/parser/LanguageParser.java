package com.neaterbits.compiler.common.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import com.neaterbits.compiler.common.antlr4.AntlrError;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.log.ParseLogger;

public interface LanguageParser {

	boolean canParseFile(File file);

	CompilationUnit parse(InputStream inputStream, Collection<AntlrError> errors, ParseLogger parseLogger) throws IOException;
	
}
