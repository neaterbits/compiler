package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.parser.ExpressionSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
