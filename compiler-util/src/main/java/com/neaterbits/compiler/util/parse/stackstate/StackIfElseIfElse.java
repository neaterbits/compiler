package com.neaterbits.compiler.util.parse.stackstate;


import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.ListStackEntry;

public final class StackIfElseIfElse<KEYWORD, CONDITION_BLOCK, BLOCK> extends ListStackEntry<CONDITION_BLOCK> {
	
	public StackIfElseIfElse(ParseLogger parseLogger) {
		super(parseLogger);
	}

	private KEYWORD elseKeyword;
	private BLOCK elseBlock;

	public KEYWORD getElseKeyword() {
		return elseKeyword;
	}

	public BLOCK getElseBlock() {
		return elseBlock;
	}

	public void setElseBlock(KEYWORD elseKeyword, BLOCK elseBlock) {
		
		Objects.requireNonNull(elseKeyword);
		Objects.requireNonNull(elseBlock);
		
		this.elseKeyword = elseKeyword;
		this.elseBlock = elseBlock;
	}
}
