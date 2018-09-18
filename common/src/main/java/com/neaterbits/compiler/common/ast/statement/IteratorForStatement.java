package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.variables.ModifiersVariableDeclarationElement;

public final class IteratorForStatement extends LoopStatement {

	private final ASTSingle<ModifiersVariableDeclarationElement> variableDeclaration;
	private final ASTSingle<Expression> collectionExpression;

	public IteratorForStatement(
			Context context,
			ModifiersVariableDeclarationElement variableDeclaration,
			Expression collectionExpression,
			Block block) {
		
		super(context, block);
		
		Objects.requireNonNull(variableDeclaration);
		Objects.requireNonNull(collectionExpression);

		this.variableDeclaration = makeSingle(variableDeclaration);
		this.collectionExpression = makeSingle(collectionExpression);
	}

	public final ModifiersVariableDeclarationElement getVariableDeclaration() {
		return variableDeclaration.get();
	}

	public final Expression getCollectionExpression() {
		return collectionExpression.get();
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onIteratorFor(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
		doIterate(variableDeclaration, recurseMode, visitor);
		doIterate(collectionExpression, recurseMode, visitor);

		super.doRecurse(recurseMode, visitor);
	}
}
