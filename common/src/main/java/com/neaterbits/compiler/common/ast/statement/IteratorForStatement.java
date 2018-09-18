package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.variables.ModifiersVariableDeclarationElement;

public final class IteratorForStatement extends LoopStatement {

	private final ModifiersVariableDeclarationElement variableDeclaration;
	private final Expression collectionExpression;

	public IteratorForStatement(
			Context context,
			ModifiersVariableDeclarationElement variableDeclaration,
			Expression collectionExpression,
			Block block) {
		
		super(context, block);
		
		Objects.requireNonNull(variableDeclaration);
		Objects.requireNonNull(collectionExpression);

		this.variableDeclaration = variableDeclaration;
		this.collectionExpression = collectionExpression;
	}

	public final ModifiersVariableDeclarationElement getVariableDeclaration() {
		return variableDeclaration;
	}

	public final Expression getCollectionExpression() {
		return collectionExpression;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onIteratorFor(this, param);
	}
}
