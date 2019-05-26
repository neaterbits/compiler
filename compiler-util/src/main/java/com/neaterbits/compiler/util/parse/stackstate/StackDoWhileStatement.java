package com.neaterbits.compiler.util.parse.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.setters.StatementSetter;

public final class StackDoWhileStatement<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY, STATEMENT>
		extends StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
		implements StatementSetter<STATEMENT> {

	private final List<STATEMENT> statements;
	
	public StackDoWhileStatement(ParseLogger parseLogger) {
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
