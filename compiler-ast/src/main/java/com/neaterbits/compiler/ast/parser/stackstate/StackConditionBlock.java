package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.parser.StatementSetter;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackConditionBlock extends StackExpressionList implements StatementSetter {

	private final List<Statement> statements;

	public StackConditionBlock(ParseLogger parseLogger) {
		super(parseLogger);

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
