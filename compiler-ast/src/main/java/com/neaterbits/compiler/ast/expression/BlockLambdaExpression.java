package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.statement.ReturnStatement;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.primitive.UnnamedVoidType;
import com.neaterbits.compiler.util.Context;

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
	public BaseType getType() {
		
		final ASTList<Statement> statements = this.block.get().getStatements();
		
		final BaseType type;
		
		if (statements.isEmpty()) {
			type = UnnamedVoidType.INSTANCE;
		}
		else if (statements.getLast() instanceof ReturnStatement) {
			
			final ReturnStatement returnStatement = (ReturnStatement)statements.getLast();

			type = returnStatement.getExpression() != null
					? returnStatement.getExpression().getType()
					: UnnamedVoidType.INSTANCE;
		}
		else {
			type = UnnamedVoidType.INSTANCE;
		}

		return type;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onBlockLambdaExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		super.doRecurse(recurseMode, iterator);
		
		doIterate(block, recurseMode, iterator);
	}
}
