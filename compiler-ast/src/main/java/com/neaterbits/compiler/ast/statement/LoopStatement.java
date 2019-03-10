package com.neaterbits.compiler.ast.statement;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public abstract class LoopStatement extends Statement {

	private final ASTSingle<Block> block;

	public LoopStatement(Context context, Block block) {
		super(context);

		this.block = makeSingle(block);
	}

	public final Block getBlock() {
		return block.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(block, recurseMode, iterator);
	}
}
