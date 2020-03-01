package com.neaterbits.compiler.java.parser;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.StackStatements;

final class StackJavaIfThen<STATEMENT> extends StackStatements<STATEMENT> {

	public StackJavaIfThen(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
