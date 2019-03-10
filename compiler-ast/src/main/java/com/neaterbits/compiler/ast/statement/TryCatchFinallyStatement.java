package com.neaterbits.compiler.ast.statement;

import java.util.List;

import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.util.Context;

public final class TryCatchFinallyStatement extends BaseTryCatchFinallyStatement {

	public TryCatchFinallyStatement(Context context, Block tryBlock, List<CatchBlock> catchBlocks, Block finallyBlock) {
		super(context, tryBlock, catchBlocks, finallyBlock);
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onTryCatchFinallyStatement(this, param);
	}
}
