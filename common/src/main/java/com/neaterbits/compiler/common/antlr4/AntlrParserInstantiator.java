package com.neaterbits.compiler.common.antlr4;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenSource;

abstract class AntlrParserInstantiator<LEXER extends Lexer, PARSER extends Parser> {

	abstract PARSER getParserForInput(ANTLRInputStream inputStream, AntlrParserFactory<LEXER, PARSER> parserFactory) throws IOException;
	
	final TokenSource createTokenSource(LEXER lexer) {
		return lexer;
	}
}
