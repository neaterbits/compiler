package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
