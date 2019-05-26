package com.neaterbits.compiler.util.parse.stackstate;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackConstantSwitchLabel<EXPRESSION, VARIABLE_REFERENCE extends EXPRESSION, PRIMARY extends EXPRESSION>
		extends StackExpression<EXPRESSION, VARIABLE_REFERENCE, PRIMARY> {

	private final String keyword;
	private final Context keywordContext;
	
	public StackConstantSwitchLabel(ParseLogger parseLogger, String keyword, Context keywordContext) {
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
