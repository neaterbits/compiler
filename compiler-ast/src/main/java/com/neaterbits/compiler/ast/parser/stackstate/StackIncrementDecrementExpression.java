package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.operator.Arithmetic;
import com.neaterbits.compiler.ast.operator.Notation;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
