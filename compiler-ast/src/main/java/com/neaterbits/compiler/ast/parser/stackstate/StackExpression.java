package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.parser.ExpressionSetter;
import com.neaterbits.compiler.ast.parser.PrimarySetter;
import com.neaterbits.compiler.ast.parser.StackEntry;
import com.neaterbits.compiler.ast.parser.VariableReferenceSetter;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackExpression extends StackEntry implements ExpressionSetter, VariableReferenceSetter, PrimarySetter {

	private Expression expression;
	
	public StackExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public void setExpression(Expression expression) {
		Objects.requireNonNull(expression);
		
		if (this.expression != null) {
			throw new IllegalStateException("expression already set " + expression.getClass());
		}
		
		this.expression = expression;
	}

	public final Expression getExpression() {
		return expression;
	}

	@Override
	public final void addExpression(Expression expression) {
		setExpression(expression);
	}

	@Override
	public void setVariableReference(VariableReference variableReference) {
		setExpression(variableReference);
	}

	@Override
	public void addPrimary(Primary primary) {
		addExpression(primary);
	}
}
