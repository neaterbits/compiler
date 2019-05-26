package com.neaterbits.compiler.util.parse.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.StackEntry;
import com.neaterbits.compiler.util.parse.stackstate.setters.ExpressionSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.VariableReferenceSetter;

public class StackExpression<EXPRESSION, VARIABLE_REFERENCE extends EXPRESSION, PRIMARY extends EXPRESSION>
	extends StackEntry
	implements ExpressionSetter<EXPRESSION>, VariableReferenceSetter<VARIABLE_REFERENCE>, PrimarySetter<PRIMARY> {

	private EXPRESSION expression;
	
	public StackExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public void setExpression(EXPRESSION expression) {
		Objects.requireNonNull(expression);
		
		if (this.expression != null) {
			throw new IllegalStateException("expression already set " + expression.getClass());
		}
		
		this.expression = expression;
	}

	public final EXPRESSION getExpression() {
		return expression;
	}

	@Override
	public final void addExpression(EXPRESSION expression) {
		setExpression(expression);
	}

	@Override
	public final void setVariableReference(VARIABLE_REFERENCE variableReference) {
		setExpression(variableReference);
	}

	@Override
	public void addPrimary(PRIMARY primary) {
		addExpression(primary);
	}
}
