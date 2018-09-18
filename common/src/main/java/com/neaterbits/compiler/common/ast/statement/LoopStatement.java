package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(block, recurseMode, visitor);
	}
}
