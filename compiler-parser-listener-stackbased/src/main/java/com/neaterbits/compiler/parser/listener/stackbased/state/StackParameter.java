package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackParameter<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY>
	extends StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> {

	public StackParameter(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
