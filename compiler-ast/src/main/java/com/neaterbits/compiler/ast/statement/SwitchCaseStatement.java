package com.neaterbits.compiler.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class SwitchCaseStatement extends ConditionStatement {

	private final ASTSingle<Keyword> keyword;
	private final ASTSingle<Expression> expression;
	private final ASTList<SwitchCaseGroup> groups;

	public SwitchCaseStatement(Context context, Keyword keyword, Expression expression, List<SwitchCaseGroup> groups) {
		super(context);
	
		Objects.requireNonNull(keyword);
		Objects.requireNonNull(expression);
		Objects.requireNonNull(groups);

		this.keyword = makeSingle(keyword);
		this.expression = makeSingle(expression);
		this.groups = makeList(groups);
	}

	public Keyword getKeyword() {
		return keyword.get();
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

		doIterate(keyword, recurseMode, iterator);
		doIterate(groups, recurseMode, iterator);
	}
}
