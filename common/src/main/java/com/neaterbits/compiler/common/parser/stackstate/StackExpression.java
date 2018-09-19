package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.StackEntry;
import com.neaterbits.compiler.common.parser.VariableReferenceSetter;

public class StackExpression extends StackEntry implements ExpressionSetter, VariableReferenceSetter {

	private Expression expression;
	
	public StackExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public void setExpression(Expression expression) {
		Objects.requireNonNull(expression);
		
		if (this.expression != null) {
			throw new IllegalStateException("expression already set");
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
}
