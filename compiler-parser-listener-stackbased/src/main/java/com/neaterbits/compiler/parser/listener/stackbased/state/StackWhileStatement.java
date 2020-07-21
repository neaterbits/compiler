package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackWhileStatement<EXPRESSION, NESTED_EXPRESSION extends EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY, STATEMENT>
	extends StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
	implements StatementSetter<STATEMENT> {

	private final List<STATEMENT> statements;
	
	public StackWhileStatement(ParseLogger parseLogger) {
		super(parseLogger);

		this.statements = new ArrayList<>();
	}

	public List<STATEMENT> getStatements() {
		return statements;
	}

	@Override
	public void addStatement(STATEMENT statement) {
		
		Objects.requireNonNull(statement);
		
		statements.add(statement);
	}
}
