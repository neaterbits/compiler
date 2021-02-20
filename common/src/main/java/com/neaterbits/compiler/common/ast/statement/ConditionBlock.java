package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class ConditionBlock extends BaseASTElement {

	private final ASTSingle<Expression> condition;
	private final ASTSingle<Block> block;
	
	public ConditionBlock(Context context, Expression condition, Block block) {
		super(context);
		
		Objects.requireNonNull(condition);
		Objects.requireNonNull(block);
		
		this.condition = makeSingle(condition);
		this.block = makeSingle(block);
	}

	public Expression getCondition() {
		return condition.get();
	}

	public Block getBlock() {
		return block.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(condition, recurseMode, iterator);
		doIterate(block, recurseMode, iterator);
	}
}
