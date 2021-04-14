package dev.nimbler.compiler.ast.objects.statement;

import java.util.List;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class TryCatchFinallyStatement extends BaseTryCatchFinallyStatement {

	public TryCatchFinallyStatement(Context context, Block tryBlock, List<CatchBlock> catchBlocks, Block finallyBlock) {
		super(context, tryBlock, catchBlocks, finallyBlock);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.TRY_CATCH_FINALLY_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onTryCatchFinallyStatement(this, param);
	}
}
