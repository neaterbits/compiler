package com.neaterbits.compiler.common.ast.statement;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;

public abstract class BaseTryCatchFinallyStatement extends Statement {

	private final Block tryBlock;
	private final List<CatchBlock> catchBlocks;
	private final Block finallyBlock;
	
	public BaseTryCatchFinallyStatement(Context context, Block tryBlock, List<CatchBlock> catchBlocks, Block finallyBlock) {
		super(context);
		
		this.tryBlock = tryBlock;
		this.catchBlocks = Collections.unmodifiableList(catchBlocks);
		this.finallyBlock = finallyBlock;
	}

	public Block getTryBlock() {
		return tryBlock;
	}

	public List<CatchBlock> getCatchBlocks() {
		return catchBlocks;
	}

	public Block getFinallyBlock() {
		return finallyBlock;
	}
}
