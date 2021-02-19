package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.ListStackEntry;

public final class StackParameterList extends ListStackEntry<Expression> implements ExpressionSetter {

	public StackParameterList(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addExpression(Expression expression) {

		Objects.requireNonNull(expression);

		add(expression);
	}
}
