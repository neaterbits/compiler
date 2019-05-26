package com.neaterbits.compiler.util.parse.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.setters.ExpressionSetter;

public final class StackForInit<TYPE_REFERENCE, EXPRESSION>
	extends BaseStackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION>
	implements ExpressionSetter<EXPRESSION> {

	private final List<EXPRESSION> expressions;
	
	public StackForInit(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.expressions = new ArrayList<>();
	}

	public List<EXPRESSION> getExpressions() {
		return expressions;
	}

	@Override
	public void addExpression(EXPRESSION expression) {

		Objects.requireNonNull(expression);
		
		expressions.add(expression);
	}
}
