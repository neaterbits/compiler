package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class ForStatement extends Statement {

	private final ASTSingle<ForInit> forInit;
	private final ASTSingle<Expression> condition;
	private final ASTSingle<ForExpressionList> forUpdate;
	
	private final ASTSingle<Block> block;

	public ForStatement(Context context, ForInit forInit, Expression condition, ForExpressionList forUpdate, Block block) {
		super(context);
		
		Objects.requireNonNull(block);
		
		this.forInit   = forInit   != null ? makeSingle(forInit)   : null;
		this.condition = condition != null ? makeSingle(condition) : null;
		this.forUpdate = forUpdate != null ? makeSingle(forUpdate) : null;

		this.block = makeSingle(block);
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

	public Block getBlock() {
		return block.get();
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onCFor(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		if (forInit != null) {
			doIterate(forInit, recurseMode, visitor);
		}
		
		if (condition != null) {
			doIterate(condition, recurseMode, visitor);
		}
		
		if (forUpdate != null) {
			doIterate(forUpdate, recurseMode, visitor);
		}
	}
}
