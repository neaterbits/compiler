package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

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
