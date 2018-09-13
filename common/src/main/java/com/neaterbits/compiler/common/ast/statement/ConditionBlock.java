package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.condition.Condition;

public final class ConditionBlock extends BaseASTElement {

	private final Condition condition;
	private final Block block;
	
	public ConditionBlock(Context context, Condition condition, Block block) {
		super(context);
		
		Objects.requireNonNull(condition);
		Objects.requireNonNull(block);
		
		this.condition = condition;
		this.block = block;
	}

	public Condition getCondition() {
		return condition;
	}

	public Block getBlock() {
		return block;
	}
}
