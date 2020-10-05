package com.neaterbits.compiler.ast.objects.statement;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.util.parse.context.Context;

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
