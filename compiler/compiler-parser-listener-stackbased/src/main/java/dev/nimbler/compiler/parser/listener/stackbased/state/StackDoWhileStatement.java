package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackDoWhileStatement<EXPRESSION, NESTED_EXPRESSION extends EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY, STATEMENT>
		extends StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
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
