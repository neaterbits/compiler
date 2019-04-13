package com.neaterbits.compiler.ast.parser.stackstate;


import java.util.Objects;

import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.ast.statement.ConditionBlock;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackIfElseIfElse extends ListStackEntry<ConditionBlock> {
	
	public StackIfElseIfElse(ParseLogger parseLogger) {
		super(parseLogger);
	}

	private Keyword elseKeyword;
	private Block elseBlock;

	public Keyword getElseKeyword() {
		return elseKeyword;
	}

	public Block getElseBlock() {
		return elseBlock;
	}

	public void setElseBlock(Keyword elseKeyword, Block elseBlock) {
		
		Objects.requireNonNull(elseKeyword);
		Objects.requireNonNull(elseBlock);
		
		this.elseKeyword = elseKeyword;
		this.elseBlock = elseBlock;
	}
}
