package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;

public final class StackResource extends BaseStackVariableDeclaration 
		implements ExpressionSetter {

	private Expression initializer;

	public StackResource(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addExpression(Expression expression) {
		if (initializer != null) {
			throw new IllegalStateException("Initializer already set");
		}
		
		this.initializer = expression;
	}

	public Expression getInitializer() {
		return initializer;
	}
}
