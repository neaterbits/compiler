package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;

public final class StackForInit extends BaseStackVariableDeclarationList implements ExpressionSetter {

	private final List<Expression> expressions;
	
	public StackForInit(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.expressions = new ArrayList<>();
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	@Override
	public void addExpression(Expression expression) {

		Objects.requireNonNull(expression);
		
		expressions.add(expression);
	}
}
