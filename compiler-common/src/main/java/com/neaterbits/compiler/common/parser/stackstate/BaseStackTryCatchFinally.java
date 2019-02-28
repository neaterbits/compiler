package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.statement.CatchBlock;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.StackEntry;

public abstract class BaseStackTryCatchFinally extends StackEntry {

	private Block tryBlock;
	private final List<CatchBlock> catchBlocks;
	private Block finallyBlock;
	
	protected BaseStackTryCatchFinally(ParseLogger parseLogger) {
		super(parseLogger);

		this.catchBlocks = new ArrayList<>();
	}

	public final Block getTryBlock() {
		return tryBlock;
	}

	public final void setTryBlock(Block tryBlock) {
		this.tryBlock = tryBlock;
	}

	public final void addCatchBlock(CatchBlock catchBlock) {
		Objects.requireNonNull(catchBlock);

		catchBlocks.add(catchBlock);
	}
	
	public final List<CatchBlock> getCatchBlocks() {
		return catchBlocks;
	}

	public final Block getFinallyBlock() {
		return finallyBlock;
	}

	public final void setFinallyBlock(Block finallyBlock) {
		this.finallyBlock = finallyBlock;
	}
}
