package dev.nimbler.compiler.antlr4;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenSource;

public class InstantiateAntlrParserInstantiator<LEXER extends Lexer, PARSER extends Parser>
		extends AntlrParserInstantiator<LEXER, PARSER> {

	@Override
	PARSER getParserForInput(ANTLRInputStream inputStream, AntlrParserFactory<LEXER, PARSER> parserFactory) {
		final LEXER lexer;
		final TokenSource tokenSource;
		final CommonTokenStream tokens;
		final PARSER parser;
		
		lexer = parserFactory.createLexer(inputStream);
		tokenSource = createTokenSource(lexer);
		tokens = new CommonTokenStream(tokenSource);
		parser = parserFactory.createParser(tokens);

		return parser;
	}
}
