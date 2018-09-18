package com.neaterbits.compiler.common.ast.statement;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(tryBlock, recurseMode, visitor);
		doIterate(catchBlocks, recurseMode, visitor);
		
		if (finallyBlock != null) {
			doIterate(finallyBlock, recurseMode, visitor);
		}
	}
}
