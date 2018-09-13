package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;

public abstract class LoopStatement extends Statement {

	private final Block block;

	public LoopStatement(Context context, Block block) {
		super(context);

		this.block = block;
	}

	public final Block getBlock() {
		return block;
	}
}
