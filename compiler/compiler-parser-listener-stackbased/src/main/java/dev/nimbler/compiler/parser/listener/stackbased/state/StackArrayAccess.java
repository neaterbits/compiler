package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackArrayAccess<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY>
		extends StackEntry
		implements PrimarySetter<PRIMARY>, VariableReferenceSetter<VARIABLE_REFERENCE>, ExpressionSetter<EXPRESSION> {

	private final List<EXPRESSION> expressions;
	
	
	public StackArrayAccess(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.expressions = new ArrayList<>();
	}

	public EXPRESSION getIndex() {
		return expressions.get(expressions.size() - 1);
	}

	@Override
	public void setVariableReference(VARIABLE_REFERENCE variableReference) {
		addPrimary(variableReference);
	}

	@Override
	public void addPrimary(PRIMARY primary) {

		Objects.requireNonNull(primary);

		addExpression(primary);
	}

	@Override
	public void addExpression(EXPRESSION expression) {

		Objects.requireNonNull(expression);

		expressions.add(expression);
	}

	public List<EXPRESSION> getExpressions() {
		return expressions;
	}
}
