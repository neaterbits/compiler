package com.neaterbits.compiler.java.parser;

import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.stackstate.StackStatements;

final class StackJavaIfThenElse extends StackStatements {

	public StackJavaIfThenElse(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
