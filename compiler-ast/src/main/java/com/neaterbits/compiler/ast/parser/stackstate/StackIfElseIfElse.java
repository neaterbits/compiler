package com.neaterbits.compiler.ast.parser.stackstate;


import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.ast.statement.ConditionBlock;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
