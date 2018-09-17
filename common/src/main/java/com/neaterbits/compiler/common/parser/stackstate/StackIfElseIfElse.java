package com.neaterbits.compiler.common.parser.stackstate;


import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.statement.ConditionBlock;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;

public final class StackIfElseIfElse extends ListStackEntry<ConditionBlock> {
	
	public StackIfElseIfElse(ParseLogger parseLogger) {
		super(parseLogger);
	}

	private Block elseBlock;

	public Block getElseBlock() {
		return elseBlock;
	}

	public void setElseBlock(Block elseBlock) {
		this.elseBlock = elseBlock;
	}
}
