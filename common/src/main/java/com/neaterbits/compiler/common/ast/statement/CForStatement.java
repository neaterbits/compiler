package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.condition.Condition;

public final class CForStatement extends LoopStatement {

	private final Statement initialStatement;
	private final Condition condition;
	private final Statement eachStatement;
	
	public CForStatement(
			Context context,
			Statement initialStatement,
			Condition condition,
			Statement eachStatement,
			Block block) {
		
		super(context, block);
		
		this.initialStatement = initialStatement;
		this.condition = condition;
		this.eachStatement = eachStatement;
	}

	public Statement getInitialStatement() {
		return initialStatement;
	}

	public Condition getCondition() {
		return condition;
	}

	public Statement getEachStatement() {
		return eachStatement;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onCFor(this, param);
	}
}

