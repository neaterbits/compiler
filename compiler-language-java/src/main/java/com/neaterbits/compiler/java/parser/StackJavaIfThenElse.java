package com.neaterbits.compiler.java.parser;

import com.neaterbits.compiler.ast.parser.stackstate.StackStatements;
import com.neaterbits.compiler.util.parse.ParseLogger;

final class StackJavaIfThenElse extends StackStatements {

	public StackJavaIfThenElse(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
