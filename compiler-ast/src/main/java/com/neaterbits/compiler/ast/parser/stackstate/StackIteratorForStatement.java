package com.neaterbits.compiler.ast.parser.stackstate;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.parser.ExpressionSetter;
import com.neaterbits.compiler.ast.parser.PrimarySetter;
import com.neaterbits.compiler.ast.parser.StatementSetter;
import com.neaterbits.compiler.ast.parser.VariableReferenceSetter;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackIteratorForStatement extends BaseStackVariableDeclaration
		implements ExpressionSetter, PrimarySetter, VariableReferenceSetter, StatementSetter {

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
	public void addPrimary(Primary primary) {
		addExpression(primary);
	}
	
	@Override
	public void setVariableReference(VariableReference variableReference) {
		addExpression(variableReference);
	}

	@Override
	public void addStatement(Statement statement) {
		Objects.requireNonNull(statement);
	}

	public List<Statement> getStatements() {
		return statements;
	}
}
