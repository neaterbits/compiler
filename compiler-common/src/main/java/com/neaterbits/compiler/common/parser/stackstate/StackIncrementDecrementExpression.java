package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.operator.Arithmetic;
import com.neaterbits.compiler.common.ast.operator.Notation;
import com.neaterbits.compiler.common.log.ParseLogger;

public final class StackIncrementDecrementExpression extends StackExpression {

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
