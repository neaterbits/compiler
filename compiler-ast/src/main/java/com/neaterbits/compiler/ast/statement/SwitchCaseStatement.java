package com.neaterbits.compiler.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

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
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(groups, recurseMode, iterator);
	}
}
