package dev.nimbler.compiler.language.java.parser.antlr;

import java.util.Arrays;
import java.util.List;import org.antlr.v4.runtime.Token;

final class JavaStatementHolder {

	private final List<Token> keywordTokens;
	private final JavaStatement statement;
	
	JavaStatementHolder(JavaStatement statement, Token ... keywordTokens) {

		this.statement = statement;
		
		this.keywordTokens = Arrays.asList(keywordTokens);
		
	}

	Token getKeywordToken(int idx) {
		return keywordTokens.get(idx);
	}

	JavaStatement getStatement() {
		return statement;
	}
}
