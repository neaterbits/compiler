package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackResource<ANNOTATION, MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, PRIMARY extends EXPRESSION>
	extends BaseStackVariableDeclaration<ANNOTATION, MODIFIER_HOLDER, TYPE_REFERENCE>
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
