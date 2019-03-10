package com.neaterbits.compiler.antlr4;

import java.util.Objects;
import java.util.function.Function;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

public final class AntlrParserFactory<LEXER extends Lexer, PARSER extends Parser> {

	private final Function<ANTLRInputStream, LEXER> createLexer;
	private final Function<TokenStream, PARSER> createParser;

	public AntlrParserFactory(Function<ANTLRInputStream, LEXER> createLexer, Function<TokenStream, PARSER> createParser) {
		
		Objects.requireNonNull(createLexer);
		Objects.requireNonNull(createParser);
		
		this.createLexer = createLexer;
		this.createParser = createParser;
	}

	public LEXER createLexer(ANTLRInputStream inputStream) {
		return createLexer.apply(inputStream);
	}

	public PARSER createParser(TokenStream tokenStream) {
		return createParser.apply(tokenStream);
	}
}
