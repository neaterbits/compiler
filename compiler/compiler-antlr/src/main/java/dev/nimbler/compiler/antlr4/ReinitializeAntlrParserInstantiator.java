package dev.nimbler.compiler.antlr4;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenSource;

final class ReinitializeAntlrParserInstantiator<LEXER extends Lexer, PARSER extends Parser>
		extends AntlrParserInstantiator<LEXER, PARSER> {

	private LEXER lexer;
	private TokenSource tokenSource;
	private CommonTokenStream tokens;
	private PARSER parser;

	private void init(AntlrParserFactory<LEXER, PARSER> parserFactory) {

		this.lexer = parserFactory.createLexer(null);
		this.tokenSource = createTokenSource(lexer);
		this.tokens = new CommonTokenStream(tokenSource);

		this.parser = parserFactory.createParser(tokens);

		parser.setTokenStream(tokens);
	}
	
	@Override
	PARSER getParserForInput(ANTLRInputStream inputStream, AntlrParserFactory<LEXER, PARSER> parserFactory) throws IOException {
		
		if (parser == null) {
			init(parserFactory);
		}
		
		lexer.setInputStream(inputStream);

		tokens.setTokenSource(tokenSource);

		parser.setTokenStream(tokens);
		
		return parser;
	}
}
