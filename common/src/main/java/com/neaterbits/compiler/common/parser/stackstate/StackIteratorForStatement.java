package com.neaterbits.compiler.common.parser.stackstate;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.StatementSetter;

public final class StackIteratorForStatement extends BaseStackVariableDeclaration
		implements ExpressionSetter, StatementSetter {

	private Expression expression;
	private final List<Statement> statements;
	
	public StackIteratorForStatement(ParseLogger parseLogger) {
		super(parseLogger);

		this.statements = new ArrayList<>();
	}

	@Override
	public void addExpression(Expression expression) {
		
		Objects.requireNonNull(expression);
		
		if (this.expression != null) {
			throw new IllegalStateException("Expression already set");
		}
		
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public void addStatement(Statement statement) {
		Objects.requireNonNull(statement);
	}

	public List<Statement> getStatements() {
		return statements;
	}
}
