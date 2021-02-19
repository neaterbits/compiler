package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class BlockLambdaExpression extends LambdaExpression {

	private final ASTSingle<Block> block;

	public BlockLambdaExpression(Context context, LambdaExpressionParameters parameters, Block block) {
		super(context, parameters);
	
		Objects.requireNonNull(block);
		
		this.block = makeSingle(block);
	}

	public Block getBlock() {
		return block.get();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onBlockLambdaExpression(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		super.doRecurse(recurseMode, visitor);
		
		doIterate(block, recurseMode, visitor);
	}
}
