package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.statement.ReturnStatement;
import com.neaterbits.compiler.ast.objects.statement.Statement;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.typereference.UnnamedVoidTypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
	public TypeReference getType() {
		
		final ASTList<Statement> statements = this.block.get().getStatements();
		
		final TypeReference type;
		
		if (statements.isEmpty()) {
			type = new UnnamedVoidTypeReference(getContext());
		}
		else if (statements.getLast() instanceof ReturnStatement) {
			
			final ReturnStatement returnStatement = (ReturnStatement)statements.getLast();

			type = returnStatement.getExpression() != null
					? returnStatement.getExpression().getType()
					: new UnnamedVoidTypeReference(getContext());
		}
		else {
			type = new UnnamedVoidTypeReference(getContext());
		}

		return type;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.BLOCK_LAMBDA_EXPRESSION;
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
