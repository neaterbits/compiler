package com.neaterbits.compiler.ast.statement;

import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public abstract class BaseTryCatchFinallyStatement extends Statement {

	private final ASTSingle<Block> tryBlock;
	private final ASTList<CatchBlock> catchBlocks;
	private final ASTSingle<Block> finallyBlock;
	
	public BaseTryCatchFinallyStatement(Context context, Block tryBlock, List<CatchBlock> catchBlocks, Block finallyBlock) {
		super(context);
		
		this.tryBlock = makeSingle(tryBlock);
		this.catchBlocks = makeList(catchBlocks);
		this.finallyBlock = finallyBlock != null ? makeSingle(finallyBlock) : null;
	}

	public final Block getTryBlock() {
		return tryBlock.get();
	}

	public final ASTList<CatchBlock> getCatchBlocks() {
		return catchBlocks;
	}

	public final Block getFinallyBlock() {
		return finallyBlock != null ? finallyBlock.get() : null;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(tryBlock, recurseMode, iterator);
		doIterate(catchBlocks, recurseMode, iterator);
		
		if (finallyBlock != null) {
			doIterate(finallyBlock, recurseMode, iterator);
		}
	}
}
