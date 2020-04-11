package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackConditionBlock<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY, STATEMENT>
		extends StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
		implements StatementSetter<STATEMENT> {

	private final Context updatedContext;
	
	private final String elseKeyword;
	private final Context elseKeywordContext;
	private final String ifKeyword;
	private final Context ifKeywordContext;
	
	private final List<STATEMENT> statements;

	public StackConditionBlock(
			ParseLogger parseLogger,
			Context updatedContext,
			String elseKeyword, Context elseKeywordContext,
			String ifKeyword, Context ifKeywordContext) {
		
		super(parseLogger);

		this.updatedContext = updatedContext;
		this.elseKeyword = elseKeyword;
		this.elseKeywordContext = elseKeywordContext;
		
		this.ifKeyword = ifKeyword;
		this.ifKeywordContext = ifKeywordContext;
		
		this.statements = new ArrayList<>();
	}

	public Context getUpdatedContext() {
		return updatedContext;
	}

	public String getElseKeyword() {
		return elseKeyword;
	}

	public Context getElseKeywordContext() {
		return elseKeywordContext;
	}

	public String getIfKeyword() {
		return ifKeyword;
	}

	public Context getIfKeywordContext() {
		return ifKeywordContext;
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
