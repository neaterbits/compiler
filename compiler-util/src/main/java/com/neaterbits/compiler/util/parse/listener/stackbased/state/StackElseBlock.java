package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackElseBlock<STATEMENT> extends StackStatements<STATEMENT> {

	private final String keyword;
	private final Context keywordContext;
	
	public StackElseBlock(ParseLogger parseLogger, String keyword, Context keywordContext) {
		super(parseLogger);
		
		Objects.requireNonNull(keyword);
		Objects.requireNonNull(keywordContext);
		
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
