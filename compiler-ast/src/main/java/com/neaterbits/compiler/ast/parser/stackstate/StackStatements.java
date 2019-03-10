package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.ast.parser.StatementSetter;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackStatements extends ListStackEntry<Statement> implements StatementSetter {

	public StackStatements(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addStatement(Statement statement) {
		
		Objects.requireNonNull(statement);
		
		super.add(statement);
	}
}
