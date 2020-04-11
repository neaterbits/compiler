package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Notation;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackIncrementDecrementExpression<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY>
		extends StackExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> {

	private final Arithmetic operator;
	private final Notation notation;
	
	public StackIncrementDecrementExpression(ParseLogger parseLogger, Arithmetic operator, Notation notation) {
		super(parseLogger);
		
		Objects.requireNonNull(operator);
		Objects.requireNonNull(notation);
		
		this.operator = operator;
		this.notation = notation;
	}

	public Arithmetic getOperator() {
		return operator;
	}

	public Notation getNotation() {
		return notation;
	}
}
