package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackParameterList<EXPRESSION>

	extends ListStackEntry<EXPRESSION>
	implements ExpressionSetter<EXPRESSION> {

	public StackParameterList(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addExpression(EXPRESSION expression) {

		Objects.requireNonNull(expression);

		add(expression);
	}
}