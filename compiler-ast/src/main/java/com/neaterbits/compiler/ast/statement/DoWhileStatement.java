package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class DoWhileStatement extends LoopStatement {

	private final ASTSingle<Expression> condition;
	
	public DoWhileStatement(Context context, Expression condition, Block block) {
		super(context, block);
		
		Objects.requireNonNull(condition);
		
		this.condition = makeSingle(condition);
	}

	public Expression getCondition() {
		return condition.get();
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onDoWhile(this, param);
	}
}
