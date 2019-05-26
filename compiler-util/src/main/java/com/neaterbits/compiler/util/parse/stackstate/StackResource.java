package com.neaterbits.compiler.util.parse.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.setters.ExpressionSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.PrimarySetter;

public final class StackResource<MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, PRIMARY extends EXPRESSION>
	extends BaseStackVariableDeclaration<MODIFIER_HOLDER, TYPE_REFERENCE>
		implements ExpressionSetter<EXPRESSION>, PrimarySetter<PRIMARY> {

	private EXPRESSION initializer;

	public StackResource(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addExpression(EXPRESSION expression) {
		if (initializer != null) {
			throw new IllegalStateException("Initializer already set");
		}
		
		this.initializer = expression;
	}
	
	@Override
	public void addPrimary(PRIMARY primary) {

		Objects.requireNonNull(primary);
		
		
		addExpression(primary);
	}

	public EXPRESSION getInitializer() {
		return initializer;
	}
}
