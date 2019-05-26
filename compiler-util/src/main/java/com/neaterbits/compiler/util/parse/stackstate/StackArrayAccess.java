package com.neaterbits.compiler.util.parse.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.StackEntry;
import com.neaterbits.compiler.util.parse.stackstate.setters.ExpressionSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.VariableReferenceSetter;

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
