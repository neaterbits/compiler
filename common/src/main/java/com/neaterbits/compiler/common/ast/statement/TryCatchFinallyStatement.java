package com.neaterbits.compiler.common.ast.statement;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;

public final class TryCatchFinallyStatement extends BaseTryCatchFinallyStatement {

	public TryCatchFinallyStatement(Context context, Block tryBlock, List<CatchBlock> catchBlocks, Block finallyBlock) {
		super(context, tryBlock, catchBlocks, finallyBlock);
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onTryCatchFinallyStatement(this, param);
	}
}
