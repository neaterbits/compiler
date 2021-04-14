package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.types.operator.IncrementDecrement;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackIncrementDecrementExpression<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY>
		extends StackExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> {

	private final IncrementDecrement operator;
	
	public StackIncrementDecrementExpression(ParseLogger parseLogger, IncrementDecrement operator) {
		super(parseLogger);
		
		Objects.requireNonNull(operator);
		
		this.operator = operator;
	}

	public IncrementDecrement getOperator() {
		return operator;
	}
}
