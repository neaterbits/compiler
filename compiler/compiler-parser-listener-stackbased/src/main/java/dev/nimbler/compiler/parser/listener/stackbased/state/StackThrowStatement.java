package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackThrowStatement<EXPRESSION, VARIABLE_REFERENCE extends EXPRESSION, PRIMARY extends EXPRESSION>
		extends StackExpression<EXPRESSION, VARIABLE_REFERENCE, PRIMARY> {

	public StackThrowStatement(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
