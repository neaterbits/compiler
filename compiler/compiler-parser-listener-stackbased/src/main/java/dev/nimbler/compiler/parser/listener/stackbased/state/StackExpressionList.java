package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.NestedExpressionSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import dev.nimbler.compiler.types.operator.Arity;
import dev.nimbler.compiler.types.operator.Operator;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class StackExpressionList<EXPRESSION, NESTED_EXPRESSION extends EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY>
		extends ListStackEntry<EXPRESSION>
		implements
				ExpressionSetter<EXPRESSION>,
				NestedExpressionSetter<NESTED_EXPRESSION>,
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
    public void addNestedExpression(NESTED_EXPRESSION expression) {

	    addExpression(expression);
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
		
		if (operator.getArity() != Arity.UNARY && operator.getArity() != Arity.BINARY) {
			throw new IllegalArgumentException("Expected unary or binary arity: " + operator);
		}

		operators.add(operator);
	}

	public final List<Operator> getOperators() {
		return operators;
	}
}
