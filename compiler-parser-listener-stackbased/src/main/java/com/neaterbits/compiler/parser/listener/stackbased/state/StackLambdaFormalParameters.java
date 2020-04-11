package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackLambdaFormalParameters extends StackEntry {

	public StackLambdaFormalParameters(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
