package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.NestedExpressionSetter;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.VariableReferenceSetter;

public class StackPrimaryList<PRIMARY, VARIABLE_REFERENCE extends PRIMARY, NESTED_EXPRESSION extends PRIMARY>
	extends ListStackEntry<PRIMARY>
	implements PrimarySetter<PRIMARY>, VariableReferenceSetter<VARIABLE_REFERENCE>, NestedExpressionSetter<NESTED_EXPRESSION> {

	public StackPrimaryList(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void setVariableReference(VARIABLE_REFERENCE variableReference) {
		addPrimary(variableReference);
	}

	@Override
	public final void addPrimary(PRIMARY primary) {
		Objects.requireNonNull(primary);

		add(primary);
	}

	@Override
	public void addNestedExpression(NESTED_EXPRESSION expression) {
		Objects.requireNonNull(expression);
		
		add(expression);
	}
}
