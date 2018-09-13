package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.condition.Condition;

public final class WhileStatement extends LoopStatement {

	private final Condition condition;
	
	public WhileStatement(Context context, Condition condition, Block block) {
		super(context, block);

		Objects.requireNonNull(condition);
		
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onWhile(this, param);
	}
}
