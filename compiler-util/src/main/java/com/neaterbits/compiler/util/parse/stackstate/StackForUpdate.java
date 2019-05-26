package com.neaterbits.compiler.util.parse.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.stackstate.setters.ExpressionSetter;

public final class StackForUpdate<EXPRESSION>
	extends ListStackEntry<EXPRESSION>
	implements ExpressionSetter<EXPRESSION> {

	public StackForUpdate(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addExpression(EXPRESSION expression) {

		Objects.requireNonNull(expression);
		
		add(expression);
	}
}
