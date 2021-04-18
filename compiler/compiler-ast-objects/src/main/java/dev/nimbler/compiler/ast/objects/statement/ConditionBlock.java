package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;

public abstract class ConditionBlock extends BaseASTElement {

	private final ASTSingle<Expression> condition;
	private final ASTSingle<Block> block;
	
	ConditionBlock(Context context, Expression condition, Block block) {
		super(context);
		
		Objects.requireNonNull(condition);
		Objects.requireNonNull(block);
		
		this.condition = makeSingle(condition);
		this.block = makeSingle(block);
	}
	
	public final Expression getCondition() {
		return condition.get();
	}

	public final Block getBlock() {
		return block.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		doIterate(condition, recurseMode, iterator);
		doIterate(block, recurseMode, iterator);
	}
}
