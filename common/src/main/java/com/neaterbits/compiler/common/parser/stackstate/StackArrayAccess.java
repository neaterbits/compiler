package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.PrimaryList;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.PrimarySetter;
import com.neaterbits.compiler.common.parser.StackEntry;
import com.neaterbits.compiler.common.parser.VariableReferenceSetter;

public final class StackArrayAccess extends StackEntry implements PrimarySetter, VariableReferenceSetter, ExpressionSetter {

	private final List<Expression> expressions;
	
	
	public StackArrayAccess(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.expressions = new ArrayList<>();
	}

	public Primary getArray(Context context) {

		final Primary primary;
		
		if (expressions.size() < 3) {
			primary = (Primary)expressions.get(0);
		}
		else {
		
			primary = new PrimaryList(context, expressions
					.subList(0, expressions.size() - 1)
					.stream()
						.map(e -> (Primary)e)
						.collect(Collectors.toList()));
		}
		
		return primary;
	}

	public Expression getIndex() {
		return expressions.get(expressions.size() - 1);
	}

	@Override
	public void setVariableReference(VariableReference variableReference) {
		addPrimary(variableReference);
	}

	@Override
	public void addPrimary(Primary primary) {

		Objects.requireNonNull(primary);

		addExpression(primary);
	}

	@Override
	public void addExpression(Expression expression) {

		Objects.requireNonNull(expression);

		expressions.add(expression);
	}
}
