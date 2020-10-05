package com.neaterbits.compiler.ast.objects.statement;

import java.util.List;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.util.parse.context.Context;

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
