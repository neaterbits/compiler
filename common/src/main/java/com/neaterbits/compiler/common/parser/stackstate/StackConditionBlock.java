package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.parser.StatementSetter;

public final class StackConditionBlock extends StackExpression implements StatementSetter {

	private final List<Statement> statements;

	public StackConditionBlock() {
		this.statements = new ArrayList<>();
	}

	public List<Statement> getStatements() {
		return statements;
	}

	@Override
	public void addStatement(Statement statement) {
		Objects.requireNonNull(statement);
	
		statements.add(statement);
	}
}
