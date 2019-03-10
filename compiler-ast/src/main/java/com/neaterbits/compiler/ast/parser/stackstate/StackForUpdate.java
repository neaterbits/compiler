package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.parser.ExpressionSetter;
import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackForUpdate extends ListStackEntry<Expression> implements ExpressionSetter {

	public StackForUpdate(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addExpression(Expression expression) {

		Objects.requireNonNull(expression);
		
		add(expression);
	}
}
