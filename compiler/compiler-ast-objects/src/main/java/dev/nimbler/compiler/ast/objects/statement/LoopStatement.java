package dev.nimbler.compiler.ast.objects.statement;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;

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
