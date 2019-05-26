package com.neaterbits.compiler.util.parse.stackstate;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.setters.ExpressionSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.StatementSetter;
import com.neaterbits.compiler.util.parse.stackstate.setters.VariableReferenceSetter;

public final class StackIteratorForStatement<
			VARIABLE_MODIFIER_HOLDER,
			TYPE_REFERENCE,
			EXPRESSION,
			PRIMARY extends EXPRESSION,
			VARIABLE_REFERENCE extends EXPRESSION,
			STATEMENT>
		extends BaseStackVariableDeclaration<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE>
		implements ExpressionSetter<EXPRESSION>, PrimarySetter<PRIMARY>, VariableReferenceSetter<VARIABLE_REFERENCE>, StatementSetter<STATEMENT> {

	private EXPRESSION expression;
	private final List<STATEMENT> statements;
	
	public StackIteratorForStatement(ParseLogger parseLogger) {
		super(parseLogger);

		this.statements = new ArrayList<>();
	}

	@Override
	public void addExpression(EXPRESSION expression) {
		
		Objects.requireNonNull(expression);
		
		if (this.expression != null) {
			throw new IllegalStateException("Expression already set");
		}
		
		this.expression = expression;
	}

	public EXPRESSION getExpression() {
		return expression;
	}

	@Override
	public void addPrimary(PRIMARY primary) {
		addExpression(primary);
	}
	
	@Override
	public void setVariableReference(VARIABLE_REFERENCE variableReference) {
		addExpression(variableReference);
	}

	@Override
	public void addStatement(STATEMENT statement) {
		Objects.requireNonNull(statement);
	}

	public List<STATEMENT> getStatements() {
		return statements;
	}
}
