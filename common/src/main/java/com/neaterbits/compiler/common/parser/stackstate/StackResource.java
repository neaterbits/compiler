package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.PrimarySetter;

public final class StackResource extends BaseStackVariableDeclaration 
		implements ExpressionSetter, PrimarySetter {

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
	
	@Override
	public void addPrimary(Primary primary) {

		Objects.requireNonNull(primary);
		
		
		addExpression(primary);
	}

	public Expression getInitializer() {
		return initializer;
	}
}
