package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.NestedExpressionSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
