package com.neaterbits.compiler.java.parser;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.StackStatements;

final class StackJavaIfThenElse<STATEMENT> extends StackStatements<STATEMENT> {

	public StackJavaIfThenElse(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
