package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.parser.StatementSetter;
import com.neaterbits.compiler.ast.statement.ForExpressionList;
import com.neaterbits.compiler.ast.statement.ForInit;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackForStatement extends StackExpressionList implements StatementSetter {

	private final String keyword;
	private final Context keywordContext;
	
	private ForInit forInit;
	private ForExpressionList forUpdate;
	
	private final List<Statement> statements;
	
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

	public ForInit getForInit() {
		return forInit;
	}

	public void setForInit(ForInit forInit) {
		
		Objects.requireNonNull(forInit);
		
		this.forInit = forInit;
	}

	public Expression getCondition(Context context) {
		return makeExpressionOrNull(context);
	}

	public ForExpressionList getForUpdate() {
		return forUpdate;
	}

	public void setForUpdate(ForExpressionList forUpdate) {
		
		Objects.requireNonNull(forUpdate);

		this.forUpdate = forUpdate;
	}

	@Override
	public void addStatement(Statement statement) {

		Objects.requireNonNull(statement);
		
		statements.add(statement);
	}

	public List<Statement> getStatements() {
		return statements;
	}
}
