package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.StackEntry;

public final class StackExpression extends StackEntry implements ExpressionSetter {

	private Expression expression;
	
	public void setExpression(Expression expression) {
		Objects.requireNonNull(expression);
		
		if (this.expression != null) {
			throw new IllegalStateException("expression already set");
		}
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public void add(Expression expression) {
		setExpression(expression);
	}
}
