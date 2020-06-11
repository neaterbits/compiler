package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class StackConditionBlock<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY, STATEMENT>
		extends StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
		implements StatementSetter<STATEMENT> {

	private final Context updatedContext;
	
	private final List<STATEMENT> statements;

	public StackConditionBlock(
			ParseLogger parseLogger,
			Context updatedContext) {
		
		super(parseLogger);

		this.updatedContext = updatedContext;
		
		this.statements = new ArrayList<>();
	}

	public final Context getUpdatedContext() {
		return updatedContext;
	}

	public final List<STATEMENT> getStatements() {
		return statements;
	}

	@Override
	public final void addStatement(STATEMENT statement) {
		Objects.requireNonNull(statement);
	
		statements.add(statement);
	}
}
