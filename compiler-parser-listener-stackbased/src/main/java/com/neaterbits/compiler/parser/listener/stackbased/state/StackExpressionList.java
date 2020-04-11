package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.operator.Arity;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackExpressionList<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY>
		extends ListStackEntry<EXPRESSION>
		implements
				ExpressionSetter<EXPRESSION>,
				VariableReferenceSetter<VARIABLE_REFERENCE>,
				PrimarySetter<PRIMARY> {

	private final List<Operator> operators;

	public StackExpressionList(ParseLogger parseLogger) {
		super(parseLogger);

		this.operators = new ArrayList<>();
	}

	@Override
	public final void addExpression(EXPRESSION expression) {
		
		Objects.requireNonNull(expression);
		
		if (getParseLogger() != null) {
			getParseLogger().onStackAddElement(expression.getClass().getSimpleName());
		}
		
		super.add(expression);
	}

	@Override
	public final void setVariableReference(VARIABLE_REFERENCE variableReference) {
		addExpression(variableReference);
	}

	@Override
	public final void addPrimary(PRIMARY primary) {
		addExpression(primary);
	}

	public final void addOperator(Operator operator) {
		Objects.requireNonNull(operator);
		
		if (operator.getArity() != Arity.BINARY) {
			throw new IllegalArgumentException("Expected binary arity: " + operator);
		}

		operators.add(operator);
	}

	public final List<Operator> getOperators(Context context) {
		return operators;
	}
}
