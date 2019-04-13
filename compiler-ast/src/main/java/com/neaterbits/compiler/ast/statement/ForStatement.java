package com.neaterbits.compiler.ast.statement;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class ForStatement extends LoopStatement {

	private final ASTSingle<ForInit> forInit;
	private final ASTSingle<Expression> condition;
	private final ASTSingle<ForExpressionList> forUpdate;

	public ForStatement(Context context, ForInit forInit, Expression condition, ForExpressionList forUpdate, Block block) {
		super(context, block);
		
		this.forInit   = forInit   != null ? makeSingle(forInit)   : null;
		this.condition = condition != null ? makeSingle(condition) : null;
		this.forUpdate = forUpdate != null ? makeSingle(forUpdate) : null;
	}

	public ForInit getForInit() {
		return forInit != null ? forInit.get() : null;
	}

	public Expression getCondition() {
		return condition != null ? condition.get() : null;
	}

	public ForExpressionList getForUpdate() {
		return forUpdate != null ? forUpdate.get() : null;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onCFor(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		if (forInit != null) {
			doIterate(forInit, recurseMode, iterator);
		}
		
		if (condition != null) {
			doIterate(condition, recurseMode, iterator);
		}
		
		if (forUpdate != null) {
			doIterate(forUpdate, recurseMode, iterator);
		}
		
		super.doRecurse(recurseMode, iterator);
	}
}
