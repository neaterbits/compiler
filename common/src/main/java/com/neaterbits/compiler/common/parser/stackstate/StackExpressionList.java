package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ExpressionList;
import com.neaterbits.compiler.common.ast.operator.Arity;
import com.neaterbits.compiler.common.ast.operator.Operator;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.ListStackEntry;

public class StackExpressionList extends ListStackEntry<Expression> implements ExpressionSetter {

	private final List<Operator> operators;

	public StackExpressionList(ParseLogger parseLogger) {
		super(parseLogger);

		this.operators = new ArrayList<>();
	}

	@Override
	public final void addExpression(Expression expression) {
		getParseLogger().onStackAddElement(expression.getClass().getSimpleName());
		
		super.add(expression);
	}

	public final void addOperator(Operator operator) {
		Objects.requireNonNull(operator);
		
		if (operator.getArity() != Arity.BINARY) {
			throw new IllegalArgumentException("Expected binary arity: " + operator);
		}

		operators.add(operator);
	}

	public final List<Operator> getOperators() {
		return operators;
	}

	public final Expression makeExpressionOrNull(Context context) {
		return getList().isEmpty()
				? null
				: makeExpression(context);
	}
	
	public final Expression makeExpression(Context context) {
		
		final List<Expression> expressions = getList();
		
		final Expression result;
		
		if (expressions.isEmpty()) {
			throw new IllegalStateException("No expressions");
		}
		else if (expressions.size() == 1) {
			result = expressions.get(0);
		}
		else {
			result = new ExpressionList(context, operators, expressions);
		}
		
		return result;
	}
}
