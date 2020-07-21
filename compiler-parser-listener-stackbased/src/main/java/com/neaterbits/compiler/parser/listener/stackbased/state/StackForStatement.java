package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackForStatement<

		EXPRESSION,
		NESTED_EXPRESSION extends EXPRESSION,
		PRIMARY extends EXPRESSION,
		VARIABLE_REFERENCE extends PRIMARY,
		STATEMENT,
		FOR_INIT>


	extends StackExpressionList<EXPRESSION, NESTED_EXPRESSION, PRIMARY, VARIABLE_REFERENCE>
	implements StatementSetter<STATEMENT> {

	private final String keyword;
	private final Context keywordContext;
	
	private FOR_INIT forInit;
	private List<EXPRESSION> forUpdate;
	
	private final List<STATEMENT> statements;
	
	public StackForStatement(ParseLogger parseLogger, String keyword, Context keywordContext) {
		super(parseLogger);
		
		Objects.requireNonNull(keyword);
		Objects.requireNonNull(keywordContext);
		
		this.keyword = keyword;
		this.keywordContext = keywordContext;
		
		this.statements = new ArrayList<>();
	}

	public String getKeyword() {
		return keyword;
	}

	public Context getKeywordContext() {
		return keywordContext;
	}

	public FOR_INIT getForInit() {
		return forInit;
	}

	public void setForInit(FOR_INIT forInit) {
		
		Objects.requireNonNull(forInit);
		
		this.forInit = forInit;
	}

	public List<EXPRESSION> getForUpdate() {
		return forUpdate;
	}

	public void setForUpdate(List<EXPRESSION> forUpdate) {
		
		Objects.requireNonNull(forUpdate);

		this.forUpdate = forUpdate;
	}

	@Override
	public void addStatement(STATEMENT statement) {

		Objects.requireNonNull(statement);
		
		statements.add(statement);
	}

	public List<STATEMENT> getStatements() {
		return statements;
	}
}
