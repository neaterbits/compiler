package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class WhileStatement extends LoopStatement {

	private final ASTSingle<Expression> condition;
	
	public WhileStatement(Context context, Expression condition, Block block) {
		super(context, block);

		Objects.requireNonNull(condition);
		
		this.condition = makeSingle(condition);
	}

	public Expression getCondition() {
		return condition.get();
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onWhile(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(condition, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
