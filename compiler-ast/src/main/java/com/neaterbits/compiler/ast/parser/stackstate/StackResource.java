package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.parser.ExpressionSetter;
import com.neaterbits.compiler.ast.parser.PrimarySetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
