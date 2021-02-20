package com.neaterbits.compiler.common.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class SwitchCaseStatement extends ConditionStatement {

	private final ASTSingle<Expression> expression;
	private final ASTList<SwitchCaseGroup> groups;

	public SwitchCaseStatement(Context context, Expression expression, List<SwitchCaseGroup> groups) {
		super(context);
	
		Objects.requireNonNull(expression);
		Objects.requireNonNull(groups);

		this.expression = makeSingle(expression);
		this.groups = makeList(groups);
	}

	public Expression getExpression() {
		return expression.get();
	}

	public ASTList<SwitchCaseGroup> getGroups() {
		return groups;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onSwitchCase(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(groups, recurseMode, visitor);
	}
}
