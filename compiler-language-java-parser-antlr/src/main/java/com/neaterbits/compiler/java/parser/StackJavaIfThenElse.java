package com.neaterbits.compiler.java.parser;

import com.neaterbits.compiler.parser.listener.stackbased.state.StackStatements;
import com.neaterbits.compiler.util.parse.ParseLogger;

final class StackJavaIfThenElse<STATEMENT> extends StackStatements<STATEMENT> {

	public StackJavaIfThenElse(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
