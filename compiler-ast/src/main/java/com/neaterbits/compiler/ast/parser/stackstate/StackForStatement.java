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

	private ForInit forInit;
	private ForExpressionList forUpdate;
	
	private final List<Statement> statements;
	
	public StackForStatement(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.statements = new ArrayList<>();
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
