package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;
import com.neaterbits.compiler.common.parser.StatementSetter;

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
