package com.neaterbits.compiler.common.ast.condition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.operator.Relational;

public final class ConditionList extends Condition {

	private final List<Relational> operators;
	private final ASTList<Expression> expressions;

	public ConditionList(Context context, List<Relational> operators, List<Expression> expressions) {
		super(context);

		Objects.requireNonNull(operators);
		Objects.requireNonNull(expressions);
		
		if (operators.size() != expressions.size() - 1) {
			throw new IllegalArgumentException("Expected one less operator than expression");
		}

		this.operators = operators;
		this.expressions = makeList(expressions);
	}

	public List<Relational> getOperators() {
		return operators;
	}

	public ASTList<Expression> getExpressions() {
		return expressions;
	}

	@Override
	public <T, R> R visit(ConditionVisitor<T, R> visitor, T param) {
		return visitor.onNestedCondition(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expressions, recurseMode, iterator);
	}
}
