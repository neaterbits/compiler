package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class ConditionExpression extends Expression {

	private final ASTSingle<Condition> condition;

	public ConditionExpression(Context context, Condition condition) {
		super(context);
		
		Objects.requireNonNull(condition);
		
		this.condition = makeSingle(condition);
	}

	public Condition getCondition() {
		return condition.get();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onConditionExpression(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(condition, recurseMode, visitor);
	}
}
