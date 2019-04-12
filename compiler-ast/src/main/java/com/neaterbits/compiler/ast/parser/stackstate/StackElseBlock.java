package com.neaterbits.compiler.ast.parser.stackstate;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackElseBlock extends StackStatements {

	private final String keyword;
	private final Context keywordContext;
	
	public StackElseBlock(ParseLogger parseLogger, String keyword, Context keywordContext) {
		super(parseLogger);
		
		this.keyword = keyword;
		this.keywordContext = keywordContext;
	}

	public String getKeyword() {
		return keyword;
	}

	public Context getKeywordContext() {
		return keywordContext;
	}
}
