package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class CForStatement extends LoopStatement {

	private final ASTSingle<Statement> initialStatement;
	private final ASTSingle<Condition> condition;
	private final ASTSingle<Statement> eachStatement;
	
	public CForStatement(
			Context context,
			Statement initialStatement,
			Condition condition,
			Statement eachStatement,
			Block block) {
		
		super(context, block);
		
		this.initialStatement = makeSingle(initialStatement);
		this.condition = makeSingle(condition);
		this.eachStatement = makeSingle(eachStatement);
	}

	public Statement getInitialStatement() {
		return initialStatement.get();
	}

	public Condition getCondition() {
		return condition.get();
	}

	public Statement getEachStatement() {
		return eachStatement.get();
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onCFor(this, param);
	}
}

